package com.example.data

import kotlin.math.ceil

/**
 * GCUF Grade Point Table and calculation utility.
 * Strictly implements the official GCUF Grade Point Table.
 */
object GradeCalculation {

    data class CourseGradeResult(
        val obtainedMarks: Double,
        val maxMarks: Int,
        val creditHours: Int,
        val qualityPoints: Double,
        val grade: String,
        val percentage: Int
    )

    /**
     * Calculates percentage rounded up to next whole number (GCUF Rule).
     */
    fun calculatePercentage(obtainedMarks: Double, maxMarks: Int): Int {
        val rawPercent = (obtainedMarks / maxMarks) * 100.0
        // Ceiling division: round to next whole number if fraction exists
        return ceil(rawPercent).toInt().coerceIn(0, 100)
    }

    /**
     * Maps obtained marks to quality points (G.P in the table) and grade name.
     * Note: G.P in the table represents Quality Points = Credit Hours * Course GP.
     */
    fun calculateCourseGrade(obtainedMarksDouble: Double, maxMarks: Int): CourseGradeResult {
        // Round obtained marks to nearest integer for table lookup, or keep as double
        val obtainedMarks = ceil(obtainedMarksDouble).toInt().coerceIn(0, maxMarks)
        val percentage = calculatePercentage(obtainedMarksDouble, maxMarks)

        val creditHours = when (maxMarks) {
            20 -> 1
            40 -> 2
            60 -> 3
            80 -> 4
            else -> 3 // Default fallback
        }

        var qualityPoints = 0.0
        var grade = "F"

        when (creditHours) {
            1 -> {
                if (obtainedMarks < 8) {
                    qualityPoints = 0.0
                    grade = "F"
                } else {
                    when (obtainedMarks) {
                        8 -> { qualityPoints = 1.00; grade = "D" }
                        9 -> { qualityPoints = 1.50; grade = "C-" }
                        10 -> { qualityPoints = 2.00; grade = "C" }
                        11 -> { qualityPoints = 2.35; grade = "C+" }
                        12 -> { qualityPoints = 2.70; grade = "B-" }
                        13 -> { qualityPoints = 3.00; grade = "B" }
                        14 -> { qualityPoints = 3.25; grade = "B+" }
                        15 -> { qualityPoints = 3.50; grade = "B+" }
                        16 -> { qualityPoints = 3.75; grade = "A-" }
                        else -> { qualityPoints = 4.00; grade = "A" } // 17 to 20
                    }
                }
            }
            2 -> {
                if (obtainedMarks < 16) {
                    qualityPoints = 0.0
                    grade = "F"
                } else {
                    when (obtainedMarks) {
                        16 -> { qualityPoints = 2.00; grade = "D" }
                        17 -> { qualityPoints = 2.60; grade = "D" }
                        18 -> { qualityPoints = 3.00; grade = "C-" }
                        19 -> { qualityPoints = 3.60; grade = "C-" }
                        20 -> { qualityPoints = 4.00; grade = "C" }
                        21 -> { qualityPoints = 4.42; grade = "C" }
                        22 -> { qualityPoints = 4.70; grade = "C+" }
                        23 -> { qualityPoints = 5.12; grade = "C+" }
                        24 -> { qualityPoints = 5.40; grade = "B-" }
                        25 -> { qualityPoints = 5.76; grade = "B-" }
                        26 -> { qualityPoints = 6.00; grade = "B" }
                        27 -> { qualityPoints = 6.30; grade = "B" }
                        28 -> { qualityPoints = 6.50; grade = "B+" }
                        29 -> { qualityPoints = 6.80; grade = "B+" }
                        30 -> { qualityPoints = 7.00; grade = "B+" }
                        31 -> { qualityPoints = 7.30; grade = "B+" }
                        32 -> { qualityPoints = 7.50; grade = "A-" }
                        33 -> { qualityPoints = 7.80; grade = "A-" }
                        else -> { qualityPoints = 8.00; grade = "A" } // 34 to 40
                    }
                }
            }
            3 -> {
                if (obtainedMarks < 24) {
                    qualityPoints = 0.0
                    grade = "F"
                } else {
                    when (obtainedMarks) {
                        24 -> { qualityPoints = 3.00; grade = "D" }
                        25 -> { qualityPoints = 3.60; grade = "D" }
                        26 -> { qualityPoints = 4.20; grade = "D" }
                        27 -> { qualityPoints = 4.50; grade = "C-" }
                        28 -> { qualityPoints = 5.10; grade = "C-" }
                        29 -> { qualityPoints = 5.70; grade = "C-" }
                        30 -> { qualityPoints = 6.00; grade = "C" }
                        31 -> { qualityPoints = 6.42; grade = "C" }
                        32 -> { qualityPoints = 6.84; grade = "C" }
                        33 -> { qualityPoints = 7.05; grade = "C+" }
                        34 -> { qualityPoints = 7.47; grade = "C+" }
                        35 -> { qualityPoints = 7.89; grade = "C+" }
                        36 -> { qualityPoints = 8.10; grade = "B-" }
                        37 -> { qualityPoints = 8.46; grade = "B-" }
                        38 -> { qualityPoints = 8.82; grade = "B-" }
                        39 -> { qualityPoints = 9.00; grade = "B" }
                        40 -> { qualityPoints = 9.30; grade = "B" }
                        41 -> { qualityPoints = 9.60; grade = "B" }
                        42 -> { qualityPoints = 9.75; grade = "B+" }
                        43 -> { qualityPoints = 10.05; grade = "B+" }
                        44 -> { qualityPoints = 10.35; grade = "B+" }
                        45 -> { qualityPoints = 10.50; grade = "B+" }
                        46 -> { qualityPoints = 10.80; grade = "B+" }
                        47 -> { qualityPoints = 11.10; grade = "B+" }
                        48 -> { qualityPoints = 11.25; grade = "A-" }
                        49 -> { qualityPoints = 11.55; grade = "A-" }
                        50 -> { qualityPoints = 11.85; grade = "A-" }
                        else -> { qualityPoints = 12.00; grade = "A" } // 51 to 60
                    }
                }
            }
            4 -> {
                if (obtainedMarks < 32) {
                    qualityPoints = 0.0
                    grade = "F"
                } else {
                    when (obtainedMarks) {
                        32 -> { qualityPoints = 4.00; grade = "D" }
                        33 -> { qualityPoints = 4.80; grade = "D" }
                        34 -> { qualityPoints = 5.20; grade = "D" }
                        35 -> { qualityPoints = 5.60; grade = "D" }
                        36 -> { qualityPoints = 6.00; grade = "C-" }
                        37 -> { qualityPoints = 6.80; grade = "C-" }
                        38 -> { qualityPoints = 7.20; grade = "C-" }
                        39 -> { qualityPoints = 7.60; grade = "C-" }
                        40 -> { qualityPoints = 8.00; grade = "C" }
                        41 -> { qualityPoints = 8.56; grade = "C" }
                        42 -> { qualityPoints = 8.84; grade = "C" }
                        43 -> { qualityPoints = 9.12; grade = "C" }
                        44 -> { qualityPoints = 9.40; grade = "C+" }
                        45 -> { qualityPoints = 9.96; grade = "C+" }
                        46 -> { qualityPoints = 10.24; grade = "C+" }
                        47 -> { qualityPoints = 10.52; grade = "C+" }
                        48 -> { qualityPoints = 10.80; grade = "B-" }
                        49 -> { qualityPoints = 11.28; grade = "B-" }
                        50 -> { qualityPoints = 11.52; grade = "B-" }
                        51 -> { qualityPoints = 11.76; grade = "B-" }
                        52 -> { qualityPoints = 12.00; grade = "B" }
                        53 -> { qualityPoints = 12.40; grade = "B" }
                        54 -> { qualityPoints = 12.60; grade = "B" }
                        55 -> { qualityPoints = 12.80; grade = "B" }
                        56 -> { qualityPoints = 13.00; grade = "B+" }
                        57 -> { qualityPoints = 13.40; grade = "B+" }
                        58 -> { qualityPoints = 13.60; grade = "B+" }
                        59 -> { qualityPoints = 13.80; grade = "B+" }
                        60 -> { qualityPoints = 14.00; grade = "B+" }
                        61 -> { qualityPoints = 14.40; grade = "B+" }
                        62 -> { qualityPoints = 14.60; grade = "B+" }
                        63 -> { qualityPoints = 14.80; grade = "B+" }
                        64 -> { qualityPoints = 15.00; grade = "A-" }
                        65 -> { qualityPoints = 15.40; grade = "A-" }
                        66 -> { qualityPoints = 15.60; grade = "A-" }
                        67 -> { qualityPoints = 15.80; grade = "A-" }
                        else -> { qualityPoints = 16.00; grade = "A" } // 68 to 80
                    }
                }
            }
        }

        return CourseGradeResult(
            obtainedMarks = obtainedMarksDouble,
            maxMarks = maxMarks,
            creditHours = creditHours,
            qualityPoints = qualityPoints,
            grade = grade,
            percentage = percentage
        )
    }

    /**
     * Calculates semester GPA from a list of course grade results.
     */
    fun calculateGPA(courses: List<CourseGradeResult>): Double {
        if (courses.isEmpty()) return 0.0
        val totalQualityPoints = courses.sumOf { it.qualityPoints }
        val totalCreditHours = courses.sumOf { it.creditHours }.toDouble()
        if (totalCreditHours == 0.0) return 0.0
        return (totalQualityPoints / totalCreditHours)
    }

    /**
     * Calculates final CGPA from list of semester GPA and credits.
     */
    fun calculateCGPA(semesters: List<Pair<Double, Int>>): Double {
        if (semesters.isEmpty()) return 0.0
        var totalQualityPoints = 0.0
        var totalCredits = 0
        for (sem in semesters) {
            totalQualityPoints += sem.first * sem.second
            totalCredits += sem.second
        }
        if (totalCredits == 0) return 0.0
        return totalQualityPoints / totalCredits
    }
}
