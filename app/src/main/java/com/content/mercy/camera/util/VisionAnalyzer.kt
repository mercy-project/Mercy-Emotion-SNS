package com.content.mercy.camera.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.content.mercy.tensorflow.Classifier
import com.content.mercy.util.ImageUtils
import com.content.mercy.util.toByteArray

/**
 * Created by rapsealk on 2019-11-02..
 */
class VisionAnalyzer(private val activity: Activity) : ImageAnalysis.Analyzer {

    interface OnImageProcessListener {
        fun onImageProcess(results: List<Classifier.Companion.Recognition>)
    }
    private var mOnImageProcessListener: OnImageProcessListener? = null
    var onImageProcessListener: OnImageProcessListener?
        get() = mOnImageProcessListener
        set(value) { mOnImageProcessListener = value }

    private val mClassifier: Classifier = Classifier.create(activity, Classifier.Device.CPU, 1)

    private var mRgbBytes: IntArray? = null

    override fun analyze(image: ImageProxy, rotationDegrees: Int) {
        val rgbBytes = mRgbBytes ?: IntArray(image.width * image.height).apply {
            mRgbBytes = this
        }

        try {
            // [1] YUV -> RGB
            val yBuffer = image.planes[0].buffer.toByteArray()
            val uBuffer = image.planes[1].buffer.toByteArray()
            val vBuffer = image.planes[2].buffer.toByteArray()
            val yuvBuffer = byteArrayOf(*yBuffer, *uBuffer, *vBuffer)
            ImageUtils.convertYUV420SPToARGB8888(yuvBuffer, image.width, image.height, rgbBytes)
            //val yuvBytes = arrayOf(yuvBuffer)

            val sensorOrientation = rotationDegrees - getScreenOrientation()
            val rgbFrameBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val croppedBitmap = Bitmap.createBitmap(
                mClassifier.getImageSizeX(),
                mClassifier.getImageSizeY(),
                Bitmap.Config.ARGB_8888)
            val frameToCropTransform = ImageUtils.getTransformationMatrix(
                image.width,
                image.height,
                mClassifier.getImageSizeX(),
                mClassifier.getImageSizeY(),
                sensorOrientation,
                MAINTAIN_ASPECT)
            val cropToFrameTransform = Matrix().apply {
                frameToCropTransform.invert(this)
            }
            rgbFrameBitmap.setPixels(rgbBytes, 0, image.width, 0, 0, image.width, image.height)

            val canvas = Canvas(croppedBitmap).apply {
                drawBitmap(rgbFrameBitmap, frameToCropTransform, null)
            }

            // Run
            val startTime = SystemClock.uptimeMillis()
            val results = mClassifier.recognizeImage(croppedBitmap)
            Log.i(TAG, "Detect: $results")
            Log.d(TAG, "crop: ${croppedBitmap.width}x${croppedBitmap.height}")
            Log.d(TAG, "processing time: ${SystemClock.uptimeMillis() - startTime}ms")

            mOnImageProcessListener?.onImageProcess(results)
        } catch (e: IllegalStateException) {

        }
    }

    private fun getScreenOrientation(): Int = when (activity.windowManager.defaultDisplay.rotation) {
        Surface.ROTATION_270 -> 270
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_90 -> 90
        else -> 0
    }

    companion object {
        private val TAG = VisionAnalyzer::class.java.simpleName
        private const val MAINTAIN_ASPECT = true
    }
}