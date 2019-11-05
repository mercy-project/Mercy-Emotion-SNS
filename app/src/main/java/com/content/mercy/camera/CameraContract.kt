package com.content.mercy.camera

import android.app.Activity
import android.content.Context
import android.util.Size
import android.view.TextureView
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import com.content.mercy.BasePresenter
import com.content.mercy.BaseView
import com.content.mercy.tensorflow.Classifier

/**
 * Created by rapsealk on 2019-11-01..
 */
interface CameraContract {

    interface View : BaseView<Presenter> {
        fun getActivity(): Activity
        fun getActivityContext(): Context
        fun getViewFinder(): TextureView
        fun updateTransform()
        fun updateTimer(seconds: Int)
        fun updateGraph(results: List<Classifier.Companion.Recognition>)
    }

    interface Presenter : BasePresenter {
        fun getResolution(): Size
        fun setResolution(resolution: Size)

        fun buildPreviewUseCase(): Preview
        fun buildAutoFitPreviewUseCase(): Preview
        fun buildImageAnalysisUseCase(): ImageAnalysis
        fun buildImageCaptureUseCase(): ImageCapture
        fun buildVideoCaptureUseCase(): VideoCapture

        fun startRecord()
        fun stopRecord()
    }
}