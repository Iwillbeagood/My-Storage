package com.example.mystorage.utils

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.io.File
import kotlin.math.sqrt

object ImageProcessingUtil {
    fun receiptImageProcessing(type: String, receiptImage: String): Bitmap? {
        val bitmap = if (type == "bitmap") {
            BitmapConverter().stringToBitmap(receiptImage)
        } else {
            DecodeFileUtil.decodeFileWithOrientation(File(receiptImage))
        }

        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        val graySrc = Mat()
        Imgproc.cvtColor(src, graySrc, Imgproc.COLOR_RGB2GRAY)

        val blurredSrc = Mat()
        Imgproc.GaussianBlur(graySrc, blurredSrc, Size(5.0, 5.0), 0.0)

        val binarySrc = Mat()
        Imgproc.adaptiveThreshold(blurredSrc, binarySrc, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11,
            2.0
        )

        // 윤곽선 찾기
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            binarySrc,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_NONE
        )

        var maxRect: Rect? = null
        var maxArea = 0.0

        for (idx in contours.indices) {
            val matOfPoint = contours[idx]
            val rect = Imgproc.boundingRect(matOfPoint)
            val area = rect.width * rect.height

            if (area > maxArea) {
                maxArea = area.toDouble()
                maxRect = rect
            }
        }

        if (maxRect == null)
            return null

        // 최대 크기의 사각형 부분을 추출
        val roi = Mat(binarySrc, maxRect)

        // 추출한 부분을 새로운 Mat 객체로 복사
        val extractedMat = Mat()
        roi.copyTo(extractedMat)

        // 추출한 부분을 비트맵으로 변환
        val extractedBitmap = Bitmap.createBitmap(extractedMat.cols(), extractedMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(extractedMat, extractedBitmap)

        return extractedBitmap
    }

}