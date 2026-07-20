package com.example.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CgpaRecord
import com.example.data.GpaRecord
import com.example.data.GradeCalculation
import com.example.data.RecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalculatorViewModel(
    application: Application,
    private val repository: RecordRepository
) : AndroidViewModel(application) {

    // SharedPreferences for settings persistence
    private val sharedPrefs = application.getSharedPreferences("resultify_prefs", Context.MODE_PRIVATE)

    // Dark Mode Setting
    private val _enableDarkMode = MutableStateFlow(sharedPrefs.getBoolean("enable_dark_mode", false))
    val enableDarkMode: StateFlow<Boolean> = _enableDarkMode.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        _enableDarkMode.value = enabled
        sharedPrefs.edit().putBoolean("enable_dark_mode", enabled).apply()
    }

    // Skip Introduction Setting
    private val _skipIntroduction = MutableStateFlow(sharedPrefs.getBoolean("skip_introduction", false))
    val skipIntroduction: StateFlow<Boolean> = _skipIntroduction.asStateFlow()

    fun setSkipIntroduction(skip: Boolean) {
        _skipIntroduction.value = skip
        sharedPrefs.edit().putBoolean("skip_introduction", skip).apply()
    }

    // App Database Records Flow
    val gpaRecords: StateFlow<List<GpaRecord>> = repository.allGpaRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cgpaRecords: StateFlow<List<CgpaRecord>> = repository.allCgpaRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // -------------------------------------------------------------
    // GPA CALCULATOR STATE & LOGIC
    // -------------------------------------------------------------
    private val _numberOfSubjects = MutableStateFlow(0)
    val numberOfSubjects: StateFlow<Int> = _numberOfSubjects.asStateFlow()

    // Temporary user input structure for GPA courses
    data class CourseInput(
        var obtainedMarks: String = "",
        var maxMarks: Int = 60 // Default corresponding to Credit Hours 3
    )

    val courseInputs = mutableStateListOf<CourseInput>()

    private val _calculatedGpaResult = MutableStateFlow<Double?>(null)
    val calculatedGpaResult: StateFlow<Double?> = _calculatedGpaResult.asStateFlow()

    private val _calculatedCourses = MutableStateFlow<List<GradeCalculation.CourseGradeResult>>(emptyList())
    val calculatedCourses: StateFlow<List<GradeCalculation.CourseGradeResult>> = _calculatedCourses.asStateFlow()

    fun setNumberOfSubjects(num: Int) {
        _numberOfSubjects.value = num
        _calculatedGpaResult.value = null
        _calculatedCourses.value = emptyList()
        
        courseInputs.clear()
        for (i in 0 until num) {
            courseInputs.add(CourseInput())
        }
    }

    fun addCourseInput() {
        courseInputs.add(CourseInput())
        _numberOfSubjects.value = courseInputs.size
        _calculatedGpaResult.value = null
    }

    fun prefillGpaRecord(record: GpaRecord) {
        val courses = record.getCourses()
        _numberOfSubjects.value = courses.size
        _calculatedGpaResult.value = null
        _calculatedCourses.value = emptyList()
        
        courseInputs.clear()
        for (course in courses) {
            val obtString = if (course.obtainedMarks % 1.0 == 0.0) {
                course.obtainedMarks.toInt().toString()
            } else {
                course.obtainedMarks.toString()
            }
            courseInputs.add(CourseInput(obtainedMarks = obtString, maxMarks = course.maxMarks))
        }
    }

    fun removeCourseInput(index: Int) {
        if (index in courseInputs.indices) {
            courseInputs.removeAt(index)
            _numberOfSubjects.value = courseInputs.size
            _calculatedGpaResult.value = null
        }
    }

    fun updateCourseObtained(index: Int, marks: String) {
        if (index in courseInputs.indices) {
            courseInputs[index] = courseInputs[index].copy(obtainedMarks = marks)
            _calculatedGpaResult.value = null
        }
    }

    fun updateCourseMax(index: Int, maxMarks: Int) {
        if (index in courseInputs.indices) {
            courseInputs[index] = courseInputs[index].copy(maxMarks = maxMarks)
            _calculatedGpaResult.value = null
        }
    }

    fun calculateGPA(): Boolean {
        // Validation: Ensure all obtained marks are valid and non-empty
        val hasErrors = courseInputs.any { input ->
            val marks = input.obtainedMarks.toDoubleOrNull()
            marks == null || marks < 0 || marks > input.maxMarks
        }
        if (hasErrors || courseInputs.isEmpty()) {
            return false
        }

        val results = courseInputs.map { input ->
            val marks = input.obtainedMarks.toDouble()
            GradeCalculation.calculateCourseGrade(marks, input.maxMarks)
        }

        _calculatedCourses.value = results
        _calculatedGpaResult.value = GradeCalculation.calculateGPA(results)
        return true
    }

    fun saveGpaRecord(name: String) {
        val gpa = _calculatedGpaResult.value ?: return
        val courses = _calculatedCourses.value
        if (courses.isEmpty()) return

        val serializedCourses = GpaRecord.serializeCourses(courses)
        viewModelScope.launch {
            repository.insertGpa(
                GpaRecord(
                    name = name.ifBlank { "Student Record" },
                    timestamp = System.currentTimeMillis(),
                    gpa = gpa,
                    coursesData = serializedCourses
                )
            )
        }
    }

    fun deleteGpaRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteGpaById(id)
        }
    }

    // -------------------------------------------------------------
    // CGPA CALCULATOR STATE & LOGIC
    // -------------------------------------------------------------
    private val _numberOfSemesters = MutableStateFlow(0)
    val numberOfSemesters: StateFlow<Int> = _numberOfSemesters.asStateFlow()

    data class SemesterInput(
        var gpa: String = "",
        var credits: Int = 20 // Default standard credits
    )

    val semesterInputs = mutableStateListOf<SemesterInput>()

    private val _calculatedCgpaResult = MutableStateFlow<Double?>(null)
    val calculatedCgpaResult: StateFlow<Double?> = _calculatedCgpaResult.asStateFlow()

    fun setNumberOfSemesters(num: Int) {
        _numberOfSemesters.value = num
        _calculatedCgpaResult.value = null

        semesterInputs.clear()
        for (i in 0 until num) {
            semesterInputs.add(SemesterInput())
        }
    }

    fun addSemesterInput() {
        semesterInputs.add(SemesterInput())
        _numberOfSemesters.value = semesterInputs.size
        _calculatedCgpaResult.value = null
    }

    fun prefillCgpaRecord(record: CgpaRecord) {
        val semesters = record.getSemesters()
        _numberOfSemesters.value = semesters.size
        _calculatedCgpaResult.value = null
        
        semesterInputs.clear()
        for (sem in semesters) {
            val gpaStr = if (sem.first % 1.0 == 0.0) {
                sem.first.toInt().toString()
            } else {
                sem.first.toString()
            }
            semesterInputs.add(SemesterInput(gpa = gpaStr, credits = sem.second))
        }
    }

    fun removeSemesterInput(index: Int) {
        if (index in semesterInputs.indices) {
            semesterInputs.removeAt(index)
            _numberOfSemesters.value = semesterInputs.size
            _calculatedCgpaResult.value = null
        }
    }

    fun updateSemesterGpa(index: Int, gpa: String) {
        if (index in semesterInputs.indices) {
            semesterInputs[index] = semesterInputs[index].copy(gpa = gpa)
            _calculatedCgpaResult.value = null
        }
    }

    fun updateSemesterCredits(index: Int, credits: Int) {
        if (index in semesterInputs.indices) {
            semesterInputs[index] = semesterInputs[index].copy(credits = credits)
            _calculatedCgpaResult.value = null
        }
    }

    fun calculateCGPA(): Boolean {
        val hasErrors = semesterInputs.any { input ->
            val gpaVal = input.gpa.toDoubleOrNull()
            gpaVal == null || gpaVal < 0.0 || gpaVal > 4.0 || input.credits <= 0
        }
        if (hasErrors || semesterInputs.isEmpty()) {
            return false
        }

        val list = semesterInputs.map { input ->
            Pair(input.gpa.toDouble(), input.credits)
        }

        _calculatedCgpaResult.value = GradeCalculation.calculateCGPA(list)
        return true
    }

    fun saveCgpaRecord(name: String) {
        val cgpa = _calculatedCgpaResult.value ?: return
        if (semesterInputs.isEmpty()) return

        val semPairs = semesterInputs.map {
            Pair(it.gpa.toDoubleOrNull() ?: 0.0, it.credits)
        }
        val serializedSemesters = CgpaRecord.serializeSemesters(semPairs)

        viewModelScope.launch {
            repository.insertCgpa(
                CgpaRecord(
                    name = name.ifBlank { "Cumulative Record" },
                    timestamp = System.currentTimeMillis(),
                    cgpa = cgpa,
                    semestersData = serializedSemesters
                )
            )
        }
    }

    fun deleteCgpaRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteCgpaById(id)
        }
    }

    // -------------------------------------------------------------
    // GENERAL RESET & CONTROL
    // -------------------------------------------------------------
    fun resetAppData() {
        viewModelScope.launch {
            repository.clearAllData()
            // Reset state
            setNumberOfSubjects(0)
            setNumberOfSemesters(0)
        }
    }
}
