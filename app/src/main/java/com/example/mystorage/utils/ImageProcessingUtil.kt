package com.example.mystorage.utils

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.io.File
import kotlin.math.sqrt

object ImageProcessingUtil {
    fun receiptImageProcessing(type: String, receiptImage: String): Bitmap {
        val bitmap = if (type == "bitmap") {
            BitmapConverter().stringToBitmap(receiptImage)
        } else {
            DecodeFileUtil.decodeFileWithOrientation(File(receiptImage))
        }

        // 1. 비트맵을 Mat 객체로 변환
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        // 2. 그레이스케일로 변환
        val graySrc = Mat()
        Imgproc.cvtColor(src, graySrc, Imgproc.COLOR_RGB2GRAY)

        // 3. 이진화
        val binarySrc = Mat()
        Imgproc.threshold(graySrc, binarySrc, 0.0, 255.0, Imgproc.THRESH_OTSU)

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

        // 가장 면적이 큰 윤곽선 찾기
        var biggestContour: MatOfPoint? = null
        var biggestContourArea: Double = 0.0
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > biggestContourArea) {
                biggestContour = contour
                biggestContourArea = area
            }
        }

        if (biggestContour == null) {
            throw IllegalArgumentException("No Contour")
        }
        // 너무 작아도 안됨
        if (biggestContourArea < 400) {
            throw IllegalArgumentException("too small")
        }

        val candidate2f = MatOfPoint2f(*biggestContour.toArray())
        val approxCandidate = MatOfPoint2f()
        Imgproc.approxPolyDP(
            candidate2f,
            approxCandidate,
            Imgproc.arcLength(candidate2f, true) * 0.02,
            true
        )

        // 사각형 판별
        if (approxCandidate.rows() != 4) {
            throw java.lang.IllegalArgumentException("It's not rectangle")
        }

        // 컨벡스(볼록한 도형)인지 판별
        if (!Imgproc.isContourConvex(MatOfPoint(*approxCandidate.toArray()))) {
            throw java.lang.IllegalArgumentException("It's not convex")
        }

        // 좌상단부터 시계 반대 방향으로 정점을 정렬한다.
        val points = arrayListOf(
            Point(approxCandidate.get(0, 0)[0], approxCandidate.get(0, 0)[1]),
            Point(approxCandidate.get(1, 0)[0], approxCandidate.get(1, 0)[1]),
            Point(approxCandidate.get(2, 0)[0], approxCandidate.get(2, 0)[1]),
            Point(approxCandidate.get(3, 0)[0], approxCandidate.get(3, 0)[1]),
        )
        points.sortBy { it.x } // x좌표 기준으로 먼저 정렬

        if (points[0].y > points[1].y) {
            val temp = points[0]
            points[0] = points[1]
            points[1] = temp
        }

        if (points[2].y < points[3].y) {
            val temp = points[2]
            points[2] = points[3]
            points[3] = temp
        }

        // 원본 영상 내 정점들
        val srcQuad = MatOfPoint2f().apply { fromList(points) }

        val maxSize = calculateMaxWidthHeight(
            tl = points[0],
            bl = points[1],
            br = points[2],
            tr = points[3]
        )
        val dw = maxSize.width
        val dh = dw * maxSize.height/maxSize.width
        val dstQuad = MatOfPoint2f(
            Point(0.0, 0.0),
            Point(0.0, dh),
            Point(dw, dh),
            Point(dw, 0.0)
        )
        // 투시변환 매트릭스 구하기
        val perspectiveTransform = Imgproc.getPerspectiveTransform(srcQuad, dstQuad)

        // 투시변환 된 결과 영상 얻기
        val dst = Mat()
        Imgproc.warpPerspective(binarySrc, dst, perspectiveTransform, Size(dw, dh))

        // dst Mat 객체를 Bitmap으로 변환
        val resultBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, resultBitmap)

        return resultBitmap
    }

    // 사각형 꼭짓점 정보로 사각형 최대 사이즈 구하기
    // 평면상 두 점 사이의 거리는 직각삼각형의 빗변길이 구하기와 동일
    private fun calculateMaxWidthHeight(
        tl: Point,
        tr: Point,
        br: Point,
        bl: Point,
    ): Size {
        // Calculate width
        val widthA = sqrt((tl.x - tr.x) * (tl.x - tr.x) + (tl.y - tr.y) * (tl.y - tr.y))
        val widthB = sqrt((bl.x - br.x) * (bl.x - br.x) + (bl.y - br.y) * (bl.y - br.y))
        val maxWidth = widthA.coerceAtLeast(widthB)
        // Calculate height
        val heightA = sqrt((tl.x - bl.x) * (tl.x - bl.x) + (tl.y - bl.y) * (tl.y - bl.y))
        val heightB = sqrt((tr.x - br.x) * (tr.x - br.x) + (tr.y - br.y) * (tr.y - br.y))
        val maxHeight = heightA.coerceAtLeast(heightB)
        return Size(maxWidth, maxHeight)
    }

    fun extractReceipt(type: String, receiptImage: String): Bitmap {
        val bitmap = if (type == "bitmap") {
            BitmapConverter().stringToBitmap(receiptImage)
        } else {
            DecodeFileUtil.decodeFileWithOrientation(File(receiptImage))
        }

        // Convert Bitmap to Mat
        val srcMat = Mat()
        Utils.bitmapToMat(bitmap, srcMat)

        // Convert to grayscale
        val grayMat = Mat()
        Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_BGR2GRAY)

        // Apply GaussianBlur
        val blurredMat = Mat()
        Imgproc.GaussianBlur(grayMat, blurredMat, Size(5.0, 5.0), 0.0)

        // Apply Canny edge detection
        val edgesMat = Mat()
        Imgproc.Canny(blurredMat, edgesMat, 50.0, 150.0)

        // Find lines using HoughLinesP
        val lines = Mat()
        Imgproc.HoughLinesP(edgesMat, lines, 1.0, Math.PI / 180, 50, 50.0, 10.0)

        // Extract corner points of receipt
        var topLeft = Point(Double.MAX_VALUE, Double.MAX_VALUE)
        var topRight = Point(Double.MIN_VALUE, Double.MAX_VALUE)
        var bottomLeft = Point(Double.MAX_VALUE, Double.MIN_VALUE)
        var bottomRight = Point(Double.MIN_VALUE, Double.MIN_VALUE)
        val corners = arrayOf(topLeft, topRight, bottomLeft, bottomRight)

        for (i in 0 until lines.rows()) {
            val line = lines.get(i, 0)
            val pt1 = Point(line[0], line[1])
            val pt2 = Point(line[2], line[3])

            // Calculate line angle
            val angle = Math.atan2((pt2.y - pt1.y), (pt2.x - pt1.x)) * 180 / Math.PI

            // Filter out horizontal and vertical lines
            if (angle < -80 || angle > 80 || (angle > -10 && angle < 10)) {
                continue
            }

            // Find intersection points of lines
            for (j in i + 1 until lines.rows()) {
                val line2 = lines.get(j, 0)
                val pt3 = Point(line2[0], line2[1])
                val pt4 = Point(line2[2], line2[3])

                val x1 = pt1.x
                val y1 = pt1.y
                val x2 = pt2.x
                val y2 = pt2.y
                val x3 = pt3.x
                val y3 = pt3.y
                val x4 = pt4.x
                val y4 = pt4.y

                val denom = ((y4 - y3) * (x2 - x1)) - ((x4 - x3) * (y2 - y1))

                // Check for parallel lines
                if (denom == 0.0) {
                    continue
                }

                val ua = (((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3))) / denom
                val ub = (((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3))) / denom

                // Check if intersection point is within the image bounds
                if (ua in 0.0..1.0 && ub >= 0 && ub <= 1) {
                    val intersection = Point(x1 + ua * (x2 - x1), y1 + ua * (y2 - y1))

                    if (intersection.x < topLeft.x && intersection.y < topLeft.y) {
                        topLeft = intersection
                    }

                    if (intersection.x > topRight.x && intersection.y < topRight.y) {
                        topRight = intersection
                    }

                    if (intersection.x < bottomLeft.x && intersection.y > bottomLeft.y) {
                        bottomLeft = intersection
                    }

                    if (intersection.x > bottomRight.x && intersection.y > bottomRight.y) {
                        bottomRight = intersection
                    }
                }
            }
        }

        // Define new Mat for cropped image
        val resultMat = Mat(Size(bitmap!!.width.toDouble(), bitmap.height.toDouble()), CvType.CV_8UC4)

        // Define corners of receipt
        val srcCorners = MatOfPoint2f(topLeft, topRight, bottomLeft, bottomRight)

        // Define new corners for cropped image
        val dstCorners = MatOfPoint2f(
            Point(0.0, 0.0),
            Point(bitmap.width.toDouble(), 0.0),
            Point(0.0, bitmap.height.toDouble()),
            Point(bitmap.width.toDouble(), bitmap.height.toDouble())
        )

        // Define transformation matrix and apply to source image
        val transformationMat = Imgproc.getPerspectiveTransform(srcCorners, dstCorners)
        Imgproc.warpPerspective(srcMat, resultMat, transformationMat, resultMat.size())

        // Convert Mat back to Bitmap and return
        val resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(resultMat, resultBitmap)

        return resultBitmap
    }
}