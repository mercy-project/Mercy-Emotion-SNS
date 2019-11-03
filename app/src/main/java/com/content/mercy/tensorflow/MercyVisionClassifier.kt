package com.content.mercy.tensorflow

import android.app.Activity

/**
 * Created by rapsealk on 2019-11-02..
 */
class MercyVisionClassifier(activity: Activity, device: Device, numThreads: Int)
    : Classifier(activity, device, numThreads) {

    companion object {
        private val TAG = MercyVisionClassifier::class.java.simpleName

        private const val IMAGE_MAX = 255f

    }
    override fun getDimPixelSize(): Int = 4

    private val labelProbArray: Array<FloatArray> = arrayOf(FloatArray(getNumLabels()))

    override fun getImageSizeX(): Int = 48

    override fun getImageSizeY(): Int = 48

    override fun getModelPath(): String = "mercy_vision_v1_1.0_48.tflite"

    override fun getLabelPath(): String = "mercy_vision_v1_1.0_48_info.txt"

    override fun getNumBytesPerChannel(): Int = 1   // Gray scaled

    override fun addPixelValue(pixelValue: Int) {
        // pixelValue: 0bRRRRRRRR_GGGGGGGG_BBBBBBBB
        val grayPixel: Float = (pixelValue.shr(16).and(0xFF)
                + pixelValue.shr(8).and(0xFF)
                + pixelValue.and(0xFF)) / 3f
        imgData.putFloat(grayPixel / IMAGE_MAX)
    }

    override fun getProbability(labelIndex: Int): Float = labelProbArray[0][labelIndex]

    override fun setProbability(labelIndex: Int, value: Number) {
        labelProbArray[0][labelIndex] = value.toFloat()
    }

    override fun getNormalizedProbability(labelIndex: Int): Float = labelProbArray[0][labelIndex]

    override fun runInference() {
        tflite?.run(imgData, labelProbArray)
    }
}