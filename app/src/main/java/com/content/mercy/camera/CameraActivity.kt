package com.content.mercy.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.WindowManager
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.content.mercy.R
import com.content.mercy.camera.util.AutoFitPreviewBuilder
import com.content.mercy.camera.util.VisionAnalyzer
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity(), CameraContract.View {

    private var mPresenter: CameraContract.Presenter = CameraPresenter(this)

    override var presenter: CameraContract.Presenter
        get() = mPresenter
        set(value) { mPresenter = value }

    private val mRequiredPermissions = arrayOf(Manifest.permission.CAMERA)
    private val mRequestCode = 0x0001

    private val mResolution1080p = Size(1080, 1920)
    private val mResolution720p = Size(720, 1280)
    private val mResolution480p = Size(480, 640)
    private lateinit var mResolution: Size

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
            window.attributes = this
        }

        if (mRequiredPermissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, mRequiredPermissions, mRequestCode)
        }

        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateTransform() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            mRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    viewFinder.post { startCamera() }
                } else {
                    throw IllegalStateException("Permission not granted.")
                    // TODO: Snackbar?
                }
            }
        }
    }

    private fun startCamera() {
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        if (metrics.widthPixels >= mResolution1080p.width) {
            mResolution = mResolution1080p
        } else {
            mResolution = mResolution480p
        }
        //mResolution = Size(metrics.widthPixels, metrics.heightPixels)

        bindCameraUseCases()
    }

    private fun bindCameraUseCases() {
        if (!getCameraWithLensFacing(CameraX.LensFacing.FRONT))
            return
        CameraX.unbindAll()
        val preview = buildAutoFitPreviewUseCase()
        val imageAnalysis = buildImageAnalysisUseCase()
        CameraX.bindToLifecycle(this, preview, imageAnalysis)
    }

    private fun buildPreviewUseCase(): Preview {
        val config = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(viewFinder.display.rotation)
        }.build()
        return Preview(config).apply {
            setOnPreviewOutputUpdateListener {
                viewFinder.surfaceTexture = it.surfaceTexture
                updateTransform()
            }
        }
    }

    private fun buildAutoFitPreviewUseCase(): Preview {
        val config = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetRotation(viewFinder.display.rotation)
            setTargetResolution(mResolution)
        }.build()
        return AutoFitPreviewBuilder.build(config, viewFinder)
    }

    private fun buildImageAnalysisUseCase(): ImageAnalysis {
        val config = ImageAnalysisConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
            setTargetResolution(mResolution)
            val analyzerThread = HandlerThread("VisionAnalysis").apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            // 매 프레임 대신 매순간 가장 최근 프레임만을 가져와 분석
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
        }.build()
        return ImageAnalysis(config).apply {
            analyzer = VisionAnalyzer(this@CameraActivity)
        }
    }

    //private fun buildImageCaptureUseCase(): ImageCapture { }

    @SuppressLint("RestrictedApi")
    private fun getCameraWithLensFacing(lensFacing: CameraX.LensFacing): Boolean {
        try {
            val cameraWithLensFacing = CameraX.getCameraWithLensFacing(lensFacing)
            Log.d(TAG, "Camera with lens facing: $cameraWithLensFacing")
            return true
        } catch (e: Exception) {
            Log.e(TAG, e.toString(), e)
            return false
        }
    }

    private fun updateTransform() {
        val matrix = Matrix()
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }   // viewFinder.display.rotation * 90
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        viewFinder.setTransform(matrix)
    }

    companion object {
        private val TAG = CameraActivity::class.java.simpleName
    }
}
