package com.example.data

import kotlinx.coroutines.flow.Flow

class RecordRepository(private val recordDao: RecordDao) {

    val allGpaRecords: Flow<List<GpaRecord>> = recordDao.getAllGpaRecords()

    suspend fun insertGpa(record: GpaRecord) {
        recordDao.insertGpaRecord(record)
    }

    suspend fun deleteGpaById(id: Int) {
        recordDao.deleteGpaRecordById(id)
    }

    val allCgpaRecords: Flow<List<CgpaRecord>> = recordDao.getAllCgpaRecords()

    suspend fun insertCgpa(record: CgpaRecord) {
        recordDao.insertCgpaRecord(record)
    }

    suspend fun deleteCgpaById(id: Int) {
        recordDao.deleteCgpaRecordById(id)
    }

    suspend fun clearAllData() {
        recordDao.clearAllData()
    }
}
