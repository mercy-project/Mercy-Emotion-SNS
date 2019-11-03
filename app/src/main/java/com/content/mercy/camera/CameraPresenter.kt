package com.content.mercy.camera

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import com.content.mercy.camera.util.AutoFitPreviewBuilder
import com.content.mercy.camera.util.VisionAnalyzer
import com.content.mercy.tensorflow.Classifier
import java.io.File

/**
 * Created by rapsealk on 2019-11-02..
 */
class CameraPresenter(private val view: CameraContract.View) : CameraContract.Presenter,
    VisionAnalyzer.OnImageProcessListener {

    private var mResoultion = Size(0, 0)

    private var mSeconds = 0
    private var mTimerHandler = Handler()
    private lateinit var mTicker: Runnable

    private var mImageCapture: ImageCapture? = null
    private var mVideoCapture: VideoCapture? = null

    init { start() }

    override fun start() {
        mTicker = Runnable {
            try {
                view.updateTimer(mSeconds)
                mSeconds += 1
            } finally {
                mTimerHandler.postDelayed(mTicker, 1000)
            }
        }
    }

    override fun getResolution(): Size = mResoultion

    override fun setResolution(resolution: Size) {
        mResoultion = resolution
    }

    override fun buildPreviewUseCase(): Preview {
        val config = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(view.getViewFinder().display.rotation)
        }.build()
        return Preview(config).apply {
            setOnPreviewOutputUpdateListener {
                view.getViewFinder().surfaceTexture = it.surfaceTexture
                view.updateTransform()
            }
        }
    }

    override fun buildAutoFitPreviewUseCase(): Preview {
        val config = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(view.getViewFinder().display.rotation)
            setTargetResolution(mResoultion)
        }.build()
        return AutoFitPreviewBuilder.build(config, view.getViewFinder())
    }

    override fun buildImageAnalysisUseCase(): ImageAnalysis {
        val config = ImageAnalysisConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(view.getViewFinder().display.rotation)
            setTargetResolution(mResoultion)
            val analyzerThread = HandlerThread("VisionAnalysis").apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            // 매 프레임 대신 매순간 가장 최근 프레임만을 가져와 분석
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
        }.build()
        return ImageAnalysis(config).apply {
            analyzer = VisionAnalyzer(view.getActivity()).apply {
                onImageProcessListener = this@CameraPresenter
            }
        }
    }

    override fun buildImageCaptureUseCase(): ImageCapture {
        val config = ImageCaptureConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(view.getViewFinder().display.rotation)
            setTargetResolution(mResoultion)
        }.build()
        return ImageCapture(config).apply {
            mImageCapture = this
        }
    }

    // FIXME: 좌우대칭
    @SuppressLint("RestrictedApi")
    override fun buildVideoCaptureUseCase(): VideoCapture {
        val config = VideoCaptureConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(view.getViewFinder().display.rotation)
            setTargetResolution(mResoultion)
        }.build()
        return VideoCapture(config).apply {
            mVideoCapture = this
        }
    }

    @SuppressLint("RestrictedApi")
    override fun startRecord() {
        val file = File(view.getActivityContext().externalMediaDirs.first(), "m${System.currentTimeMillis()}.mp4")
        Log.d(TAG, "File: $file, Path: ${file.path}")
        mVideoCapture?.startRecording(file, object : VideoCapture.OnVideoSavedListener {
            override fun onVideoSaved(file: File) {
                Log.d(TAG, "File is saved: ${file.absolutePath}")
            }

            override fun onError(videoCaptureError: VideoCapture.VideoCaptureError, message: String, cause: Throwable?) {
                Log.e(TAG, videoCaptureError.toString(), cause)
                Log.e(TAG, "Message: $message")
            }
        })
        mSeconds = 0
        mTicker.run()
    }

    @SuppressLint("RestrictedApi")
    override fun stopRecord() {
        mTimerHandler.removeCallbacksAndMessages(null)
        mVideoCapture?.stopRecording()
    }

    override fun onImageProcess(results: List<Classifier.Companion.Recognition>) {
        view.updateGraph(results)
    }

    companion object {
        private val TAG = CameraPresenter::class.java.simpleName
    }
}