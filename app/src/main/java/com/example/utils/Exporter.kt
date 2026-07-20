package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.example.data.CgpaRecord
import com.example.data.GpaRecord
import com.example.data.GradeCalculation
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Exporter {

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Renders a GPA Record onto a canvas.
     * Can be used for both Bitmap (PNG/JPG) and PDF Canvas.
     * Width and height are scaled to match the canvas size.
     */
    private fun renderGpaReport(
        canvas: Canvas,
        width: Float,
        height: Float,
        record: GpaRecord
    ) {
        // Base padding and styling
        val padding = width * 0.08f
        val contentWidth = width - (padding * 2)

        // Paints
        val backgroundPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        val primaryPaint = Paint().apply {
            color = Color.parseColor("#0C50B5") // Resultify Blue
            isAntiAlias = true
        }
        val textPrimaryPaint = Paint().apply {
            color = Color.parseColor("#1C1B1F")
            textSize = width * 0.032f
            isAntiAlias = true
        }
        val gridLinePaint = Paint().apply {
            color = Color.parseColor("#4C8DFF") // Beautiful Resultify Secondary Blue for colored lines
            strokeWidth = 1.5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        val rowBackgroundPaint = Paint().apply {
            color = Color.parseColor("#F5F7FA")
            style = Paint.Style.FILL
        }

        // Draw white background
        canvas.drawRect(0f, 0f, width, height, backgroundPaint)

        // Draw App Name Title Header
        val titlePaint = Paint().apply {
            color = Color.parseColor("#0C50B5")
            textSize = width * 0.065f
            isAntiAlias = true
            isFakeBoldText = true
        }
        val subtitlePaint = Paint().apply {
            color = Color.parseColor("#555555")
            textSize = width * 0.026f
            isAntiAlias = true
        }
        val metaPaint = Paint().apply {
            color = Color.parseColor("#1C1B1F")
            textSize = width * 0.028f
            isAntiAlias = true
        }

        // Draw App Name
        canvas.drawText("Resultify", padding, padding * 1.1f, titlePaint)

        // Draw Subtitle
        canvas.drawText("Official Semester GPA Report & Grade Tracker", padding, padding * 1.45f, subtitlePaint)

        // Draw Beautiful Separation Line Under App Header
        val headerLinePaint = Paint().apply {
            color = Color.parseColor("#0C50B5")
            strokeWidth = width * 0.003f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawLine(padding, padding * 1.65f, width - padding, padding * 1.65f, headerLinePaint)

        // Draw Record Details
        canvas.drawText("Record: ${record.name}", padding, padding * 2.05f, metaPaint)
        val dateStr = formatDate(record.timestamp)
        val dateWidth = metaPaint.measureText("Date: $dateStr")
        canvas.drawText("Date: $dateStr", width - padding - dateWidth, padding * 2.05f, metaPaint)

        // Draw Course Table
        var currentY = padding * 2.7f
        val tableTopY = currentY
        val colWidths = floatArrayOf(
            contentWidth * 0.1f,  // #
            contentWidth * 0.25f, // Obt. Marks
            contentWidth * 0.22f, // Total Marks
            contentWidth * 0.18f, // QP
            contentWidth * 0.15f, // Grade
            contentWidth * 0.1f   // %
        )
        val colNames = arrayOf("S#", "Obt. Marks", "Total Marks", "QP", "Grade", "%")

        // Draw Table Header Background
        rowBackgroundPaint.color = Color.parseColor("#0C50B5")
        canvas.drawRoundRect(
            RectF(padding, tableTopY - (padding * 0.35f), width - padding, tableTopY + (padding * 0.35f)),
            8f, 8f, rowBackgroundPaint
        )

        // Draw Header Text
        var colX = padding
        textPrimaryPaint.color = Color.WHITE
        textPrimaryPaint.textSize = width * 0.03f
        textPrimaryPaint.isFakeBoldText = true
        for (i in colNames.indices) {
            canvas.drawText(colNames[i], colX + 8f, tableTopY + (padding * 0.1f), textPrimaryPaint)
            colX += colWidths[i]
        }

        // Draw Courses Rows
        currentY += padding * 0.4f
        val courses = record.getCourses()
        textPrimaryPaint.color = Color.parseColor("#1C1B1F")
        textPrimaryPaint.isFakeBoldText = false
        rowBackgroundPaint.color = Color.parseColor("#F5F7FA")

        courses.forEachIndexed { index, course ->
            currentY += padding * 0.6f
            val rowY = currentY

            // Draw Zebra striping
            if (index % 2 == 1) {
                canvas.drawRect(padding, rowY - (padding * 0.3f), width - padding, rowY + (padding * 0.3f), rowBackgroundPaint)
            }

            // Draw Row content
            var cellX = padding
            val cells = arrayOf(
                (index + 1).toString(),
                String.format(Locale.getDefault(), "%.1f", course.obtainedMarks),
                course.maxMarks.toString(),
                String.format(Locale.getDefault(), "%.2f", course.qualityPoints),
                course.grade,
                "${course.percentage}%"
            )

            for (i in cells.indices) {
                canvas.drawText(cells[i], cellX + 8f, rowY + (padding * 0.1f), textPrimaryPaint)
                cellX += colWidths[i]
            }

            // Draw beautiful color line below row
            gridLinePaint.strokeWidth = 1f
            canvas.drawLine(padding, rowY + (padding * 0.3f), width - padding, rowY + (padding * 0.3f), gridLinePaint)
        }

        // Draw final GPA Badge
        currentY += padding * 1.2f
        val gpaBoxRect = RectF(padding, currentY - (padding * 0.6f), width - padding, currentY + (padding * 0.6f))
        primaryPaint.color = Color.parseColor("#EAF2FF")
        canvas.drawRoundRect(gpaBoxRect, 12f, 12f, primaryPaint)

        // Border around GPA badge
        gridLinePaint.color = Color.parseColor("#0C50B5")
        gridLinePaint.strokeWidth = 3f
        canvas.drawRoundRect(gpaBoxRect, 12f, 12f, gridLinePaint)

        // GPA Text inside Badge
        primaryPaint.color = Color.parseColor("#0C50B5")
        primaryPaint.textSize = width * 0.055f
        primaryPaint.isFakeBoldText = true
        val gpaText = String.format(Locale.getDefault(), "FINAL GPA: %.2f", record.gpa)
        val textWidth = primaryPaint.measureText(gpaText)
        canvas.drawText(gpaText, (width - textWidth) / 2f, currentY + (padding * 0.18f), primaryPaint)
    }

    /**
     * Renders a CGPA Record onto a canvas.
     */
    private fun renderCgpaReport(
        canvas: Canvas,
        width: Float,
        height: Float,
        record: CgpaRecord
    ) {
        // Base padding and styling
        val padding = width * 0.08f
        val contentWidth = width - (padding * 2)

        // Paints
        val backgroundPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        val primaryPaint = Paint().apply {
            color = Color.parseColor("#0C50B5")
            isAntiAlias = true
        }
        val textPrimaryPaint = Paint().apply {
            color = Color.parseColor("#1C1B1F")
            textSize = width * 0.032f
            isAntiAlias = true
        }
        val gridLinePaint = Paint().apply {
            color = Color.parseColor("#4C8DFF") // Beautiful Resultify Secondary Blue for colored lines
            strokeWidth = 1.5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        val rowBackgroundPaint = Paint().apply {
            color = Color.parseColor("#F5F7FA")
            style = Paint.Style.FILL
        }

        // Draw white background
        canvas.drawRect(0f, 0f, width, height, backgroundPaint)

        // Draw App Name Title Header
        val titlePaint = Paint().apply {
            color = Color.parseColor("#0C50B5")
            textSize = width * 0.065f
            isAntiAlias = true
            isFakeBoldText = true
        }
        val subtitlePaint = Paint().apply {
            color = Color.parseColor("#555555")
            textSize = width * 0.026f
            isAntiAlias = true
        }
        val metaPaint = Paint().apply {
            color = Color.parseColor("#1C1B1F")
            textSize = width * 0.028f
            isAntiAlias = true
        }

        // Draw App Name
        canvas.drawText("Resultify", padding, padding * 1.1f, titlePaint)

        // Draw Subtitle
        canvas.drawText("Official Cumulative CGPA Report & Grade Tracker", padding, padding * 1.45f, subtitlePaint)

        // Draw Beautiful Separation Line Under App Header
        val headerLinePaint = Paint().apply {
            color = Color.parseColor("#0C50B5")
            strokeWidth = width * 0.003f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawLine(padding, padding * 1.65f, width - padding, padding * 1.65f, headerLinePaint)

        // Draw Record Details
        canvas.drawText("Record: ${record.name}", padding, padding * 2.05f, metaPaint)
        val dateStr = formatDate(record.timestamp)
        val dateWidth = metaPaint.measureText("Date: $dateStr")
        canvas.drawText("Date: $dateStr", width - padding - dateWidth, padding * 2.05f, metaPaint)

        // Draw Semester Table
        var currentY = padding * 2.7f
        val tableTopY = currentY
        val colWidths = floatArrayOf(
            contentWidth * 0.25f, // Sem #
            contentWidth * 0.45f, // GPA
            contentWidth * 0.3f   // Credits
        )
        val colNames = arrayOf("Semester #", "GPA Obtained", "Credits Completed")

        // Draw Table Header Background
        rowBackgroundPaint.color = Color.parseColor("#0C50B5")
        canvas.drawRoundRect(
            RectF(padding, tableTopY - (padding * 0.35f), width - padding, tableTopY + (padding * 0.35f)),
            8f, 8f, rowBackgroundPaint
        )

        // Draw Header Text
        var colX = padding
        textPrimaryPaint.color = Color.WHITE
        textPrimaryPaint.textSize = width * 0.032f
        textPrimaryPaint.isFakeBoldText = true
        for (i in colNames.indices) {
            canvas.drawText(colNames[i], colX + 8f, tableTopY + (padding * 0.1f), textPrimaryPaint)
            colX += colWidths[i]
        }

        // Draw Semester Rows
        currentY += padding * 0.4f
        val semesters = record.getSemesters()
        textPrimaryPaint.color = Color.parseColor("#1C1B1F")
        textPrimaryPaint.isFakeBoldText = false
        rowBackgroundPaint.color = Color.parseColor("#F5F7FA")

        semesters.forEachIndexed { index, sem ->
            currentY += padding * 0.6f
            val rowY = currentY

            if (index % 2 == 1) {
                canvas.drawRect(padding, rowY - (padding * 0.3f), width - padding, rowY + (padding * 0.3f), rowBackgroundPaint)
            }

            var cellX = padding
            val cells = arrayOf(
                "Semester ${index + 1}",
                String.format(Locale.getDefault(), "%.2f", sem.first),
                sem.second.toString()
            )

            for (i in cells.indices) {
                canvas.drawText(cells[i], cellX + 8f, rowY + (padding * 0.1f), textPrimaryPaint)
                cellX += colWidths[i]
            }

            // Draw beautiful color line below row
            gridLinePaint.strokeWidth = 1f
            canvas.drawLine(padding, rowY + (padding * 0.3f), width - padding, rowY + (padding * 0.3f), gridLinePaint)
        }

        // Draw final CGPA Badge
        currentY += padding * 1.2f
        val cgpaBoxRect = RectF(padding, currentY - (padding * 0.6f), width - padding, currentY + (padding * 0.6f))
        primaryPaint.color = Color.parseColor("#EAF2FF")
        canvas.drawRoundRect(cgpaBoxRect, 12f, 12f, primaryPaint)

        // Border around CGPA badge
        gridLinePaint.color = Color.parseColor("#0C50B5")
        gridLinePaint.strokeWidth = 3f
        canvas.drawRoundRect(cgpaBoxRect, 12f, 12f, gridLinePaint)

        // CGPA Text inside Badge
        primaryPaint.color = Color.parseColor("#0C50B5")
        primaryPaint.textSize = width * 0.055f
        primaryPaint.isFakeBoldText = true
        val cgpaText = String.format(Locale.getDefault(), "FINAL CGPA: %.2f", record.cgpa)
        val textWidth = primaryPaint.measureText(cgpaText)
        canvas.drawText(cgpaText, (width - textWidth) / 2f, currentY + (padding * 0.18f), primaryPaint)
    }

    /**
     * Exports a GPA record to PDF.
     */
    fun exportGpaToPdf(context: Context, uri: Uri, record: GpaRecord): Boolean {
        return try {
            val contentResolver = context.contentResolver
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream == null) return false

            val pdfDocument = PdfDocument()
            // Standard A4 dimensions in postscript points (595 x 842)
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            renderGpaReport(page.canvas, 595f, 842f, record)

            pdfDocument.finishPage(page)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.flush()
            outputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Exports a GPA record as a high-res PNG Image.
     */
    fun exportGpaToPng(context: Context, uri: Uri, record: GpaRecord): Boolean {
        return try {
            val contentResolver = context.contentResolver
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream == null) return false

            // Standard high-res image dimensions (1200 x 1700)
            val bitmap = Bitmap.createBitmap(1200, 1700, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            renderGpaReport(canvas, 1200f, 1700f, record)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            bitmap.recycle()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Exports a CGPA record to PDF.
     */
    fun exportCgpaToPdf(context: Context, uri: Uri, record: CgpaRecord): Boolean {
        return try {
            val contentResolver = context.contentResolver
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream == null) return false

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            renderCgpaReport(page.canvas, 595f, 842f, record)

            pdfDocument.finishPage(page)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.flush()
            outputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Exports a CGPA record to PNG Image.
     */
    fun exportCgpaToPng(context: Context, uri: Uri, record: CgpaRecord): Boolean {
        return try {
            val contentResolver = context.contentResolver
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream == null) return false

            val bitmap = Bitmap.createBitmap(1200, 1700, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            renderCgpaReport(canvas, 1200f, 1700f, record)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            bitmap.recycle()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
