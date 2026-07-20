package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Query("SELECT * FROM gpa_records ORDER BY timestamp DESC")
    fun getAllGpaRecords(): Flow<List<GpaRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGpaRecord(record: GpaRecord)

    @Query("DELETE FROM gpa_records WHERE id = :id")
    suspend fun deleteGpaRecordById(id: Int)

    @Query("SELECT * FROM cgpa_records ORDER BY timestamp DESC")
    fun getAllCgpaRecords(): Flow<List<CgpaRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCgpaRecord(record: CgpaRecord)

    @Query("DELETE FROM cgpa_records WHERE id = :id")
    suspend fun deleteCgpaRecordById(id: Int)

    @Query("DELETE FROM gpa_records")
    suspend fun clearGpaRecords()

    @Query("DELETE FROM cgpa_records")
    suspend fun clearCgpaRecords()

    suspend fun clearAllData() {
        clearGpaRecords()
        clearCgpaRecords()
    }
}
