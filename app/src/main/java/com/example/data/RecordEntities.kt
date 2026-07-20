package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gpa_records")
data class GpaRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val timestamp: Long,
    val gpa: Double,
    val coursesData: String // Serialized course list
) {
    fun getCourses(): List<GradeCalculation.CourseGradeResult> {
        if (coursesData.isBlank()) return emptyList()
        return coursesData.split(";").mapNotNull { row ->
            val parts = row.split(",")
            if (parts.size < 6) null else {
                GradeCalculation.CourseGradeResult(
                    obtainedMarks = parts[0].toDoubleOrNull() ?: 0.0,
                    maxMarks = parts[1].toIntOrNull() ?: 60,
                    creditHours = parts[2].toIntOrNull() ?: 3,
                    qualityPoints = parts[3].toDoubleOrNull() ?: 0.0,
                    grade = parts[4],
                    percentage = parts[5].toIntOrNull() ?: 0
                )
            }
        }
    }

    companion object {
        fun serializeCourses(courses: List<GradeCalculation.CourseGradeResult>): String {
            return courses.joinToString(";") { course ->
                "${course.obtainedMarks},${course.maxMarks},${course.creditHours},${course.qualityPoints},${course.grade},${course.percentage}"
            }
        }
    }
}

@Entity(tableName = "cgpa_records")
data class CgpaRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val timestamp: Long,
    val cgpa: Double,
    val semestersData: String // Serialized semester list
) {
    fun getSemesters(): List<Pair<Double, Int>> {
        if (semestersData.isBlank()) return emptyList()
        return semestersData.split(";").mapNotNull { row ->
            val parts = row.split(",")
            if (parts.size < 2) null else {
                val gpa = parts[0].toDoubleOrNull() ?: 0.0
                val credits = parts[1].toIntOrNull() ?: 3
                Pair(gpa, credits)
            }
        }
    }

    companion object {
        fun serializeSemesters(semesters: List<Pair<Double, Int>>): String {
            return semesters.joinToString(";") { "${it.first},${it.second}" }
        }
    }
}
