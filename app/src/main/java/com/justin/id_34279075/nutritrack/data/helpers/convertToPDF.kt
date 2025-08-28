package com.justin.id_34279075.nutritrack.data.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * This is an implemented extra features that converts insights collected at the Clinician screen to pdf for references purposes.
 */
fun convertToPDF(context: Context, plainText: String, fileName: String): File? {

    val pdf = PdfDocument()

    val pageWidth = 595
    val pageHeight = 842

    // Usage of A4 page size for compatibility
    val pageInformation = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

    var page = pdf.startPage(pageInformation)
    var canvas = page.canvas

    // Paint will be use to render text on the screen
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 12f
        isAntiAlias = true
        typeface = Typeface.SANS_SERIF
    }

    // Set boundaries for the page
    val marginLeft = 80f
    val marginTop = 70f
    val marginRight = 80f
    val marginBottom = 70f

    var pageNumber = 1

    val maxWidth = pageInformation.pageWidth - 2 * marginLeft

    // Split the plaintext by new line character
    val textWithLines = plainText.split("\n")
    var yPosition = marginTop

    // Draw each line, while updating the vertical position
    for (line in textWithLines) {
        yPosition = drawTextOnScreen(canvas, line, maxWidth, paint,
            marginLeft, yPosition)
        if (yPosition + paint.textSize > pageHeight - marginBottom) {
            pdf.finishPage(page)
            pageNumber++
            page = pdf.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            canvas = page.canvas
            yPosition = marginTop
        } else {
            yPosition += paint.textSize
        }
    }

    // Add report generated time to the footer
    val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    drawTextOnScreen(canvas, "Report generated at $dateTime", maxWidth, paint, marginLeft, yPosition + paint.textSize)

    pdf.finishPage(page)

    // File will be saved in a report + year + month + date + minutes + seconds format to prevent duplication
    val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val createFile = File(downloads, "${fileName}_$timestamp.pdf")
    try {
        pdf.writeTo(FileOutputStream(createFile))
    } catch (e: Exception) {
        return null
    } finally {
        pdf.close()
    }

    return createFile
}

// This function will handle the main procedure of drawing on the screen
fun drawTextOnScreen(canvas: Canvas, plainText: String, maxWidth: Float, paint: Paint,
                     startXPosition: Float, startYPosition: Float ): Float {

    // For each line split each words through the " " delimeter
    val tokens = plainText.split(" ")
    var result = ""
    var y = startYPosition

    for (token in tokens) {
        val testLine = if (result.isEmpty()) token else "$result $token"
        val textWidth = paint.measureText(testLine)

        // Check if we hit right boundary, if so move to next line
        if (textWidth > maxWidth) {
            canvas.drawText(result, startXPosition, y, paint)
            y += paint.textSize * 1.3f
            result = token
        } else {
            result = testLine
        }
    }

    // Handles the remaining text if available, due to text wrap from the code above
    if (result.isNotEmpty()) {
        canvas.drawText(result, startXPosition, y, paint)
        y += paint.textSize * 1.3f
    }
    return y
}