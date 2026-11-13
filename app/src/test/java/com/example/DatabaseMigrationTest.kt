package com.example

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.NoteRecord
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DatabaseMigrationTest {

    @Test
    fun `migrating from v1 database preserves gpa and cgpa records`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbName = "test_migration_v1_to_v3.db"
        context.deleteDatabase(dbName)

        // Create an exact v1 database (gpa_records + cgpa_records only)
        val helper = object : SQLiteOpenHelper(context, dbName, null, 1) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `gpa_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `gpa` REAL NOT NULL, `coursesData` TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `cgpa_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `cgpa` REAL NOT NULL, `semestersData` TEXT NOT NULL)"
                )
                db.execSQL(
                    "INSERT INTO `gpa_records` (`id`, `name`, `timestamp`, `gpa`, `coursesData`) VALUES (1, 'Sem 1 GPA', 1700000000000, 3.75, '50.0,60,3,11.25,A,83')"
                )
                db.execSQL(
                    "INSERT INTO `cgpa_records` (`id`, `name`, `timestamp`, `cgpa`, `semestersData`) VALUES (1, 'Overall CGPA', 1700000001000, 3.65, '3.60,18;3.70,18')"
                )
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
        }
        helper.writableDatabase.close()
        helper.close()

        val roomDb = Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_1_3)
            .build()

        try {
            val dao = roomDb.recordDao()
            val gpaList = dao.getAllGpaRecords().first()
            val cgpaList = dao.getAllCgpaRecords().first()

            assertEquals(1, gpaList.size)
            assertEquals("Sem 1 GPA", gpaList[0].name)
            assertEquals(3.75, gpaList[0].gpa, 0.001)

            assertEquals(1, cgpaList.size)
            assertEquals("Overall CGPA", cgpaList[0].name)
            assertEquals(3.65, cgpaList[0].cgpa, 0.001)

            // Verify new notes table works after migration
            dao.insertNote(NoteRecord(title = "Target", content = "Keep GPA above 3.7", timestamp = 1700000002000))
            val notes = dao.getAllNotes().first()
            assertEquals(1, notes.size)
            assertEquals("Target", notes[0].title)
        } finally {
            roomDb.close()
            context.deleteDatabase(dbName)
        }
    }

    @Test
    fun `migrating from v2 database preserves notes and existing records`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbName = "test_migration_v2_to_v3.db"
        context.deleteDatabase(dbName)

        // Create an exact v2 database (notes without title column)
        val helper = object : SQLiteOpenHelper(context, dbName, null, 2) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `gpa_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `gpa` REAL NOT NULL, `coursesData` TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `cgpa_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `cgpa` REAL NOT NULL, `semestersData` TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)"
                )
                db.execSQL(
                    "INSERT INTO `notes` (`id`, `content`, `timestamp`) VALUES (1, 'Remember lab assignment', 1700000000000)"
                )
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
        }
        helper.writableDatabase.close()
        helper.close()

        val roomDb = Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_1_3)
            .build()

        try {
            val dao = roomDb.recordDao()
            val notes = dao.getAllNotes().first()
            assertEquals(1, notes.size)
            assertEquals("Remember lab assignment", notes[0].content)
            assertEquals("", notes[0].title)
        } finally {
            roomDb.close()
            context.deleteDatabase(dbName)
        }
    }
}
