package com.content.mercy.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.content.mercy.R
import com.content.mercy.camera.util.Resolution
import com.content.mercy.tensorflow.Classifier
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity(), CameraContract.View {

    private var mPresenter: CameraContract.Presenter = CameraPresenter(this)

    override var presenter: CameraContract.Presenter
        get() = mPresenter
        set(value) { mPresenter = value }

    private val mRequiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )
    private val mRequestCode = 0x0001

    // TODO: move to res/string-array
    private val emotions = arrayListOf(
        "anger",
        "disgust/contempt",
        "afraid",
        "happiness",
        "sadness",
        "surprise",
        "neutral"
    )
    private val mGraphData = arrayListOf(
        BarEntry(0f, 0f),
        BarEntry(1f, 0f),
        BarEntry(2f, 0f),
        BarEntry(3f, 0f),
        BarEntry(4f, 0f),
        BarEntry(5f, 0f),
        BarEntry(6f, 0f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
            window.attributes = this
        }

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        if (mRequiredPermissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, mRequiredPermissions, mRequestCode)
        }

        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateTransform() }

        /* FIXME: 이미지 크기 변화 문제가 있다..
        recordButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mPresenter.startRecord()
            } else {
                mPresenter.stopRecord()
            }
        }
        */

        setGraph()
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

    override fun getActivity(): Activity = this

    override fun getActivityContext(): Context = this

    override fun getViewFinder(): TextureView = viewFinder

    override fun updateTimer(seconds: Int) {
        /*
        runOnUiThread {
            timerView.text = String.format(
                getString(R.string.recording_time,
                    seconds / 60,
                    seconds % 60))
        }
        */
    }

    private fun startCamera() {
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val resolution = when {
            metrics.widthPixels >= Resolution.RESOLUTION_1080P.width -> Resolution.RESOLUTION_1080P
            metrics.widthPixels >= Resolution.RESOLUTION_720P.width -> Resolution.RESOLUTION_720P
            else -> Resolution.RESOLUTION_480P
        }
        mPresenter.setResolution(resolution)

        bindCameraUseCases()
    }

    private fun bindCameraUseCases() {
        if (!getCameraWithLensFacing(CameraX.LensFacing.FRONT))
            return
        CameraX.unbindAll()
        val preview = mPresenter.buildAutoFitPreviewUseCase()
        val imageAnalysis = mPresenter.buildImageAnalysisUseCase()
        val imageCapture = mPresenter.buildImageCaptureUseCase()
        val videoCapture = mPresenter.buildVideoCaptureUseCase()
        CameraX.bindToLifecycle(this, preview, imageAnalysis, imageCapture/*, videoCapture*/)
    }

    @SuppressLint("RestrictedApi")
    private fun getCameraWithLensFacing(lensFacing: CameraX.LensFacing): Boolean {
        var returnCode = false
        try {
            val cameraWithLensFacing = CameraX.getCameraWithLensFacing(lensFacing)
            Log.d(TAG, "Camera with lens facing: $cameraWithLensFacing")
            returnCode = true
        } catch (e: Exception) {
            Log.e(TAG, e.toString(), e)
        }
        return returnCode
    }

    override fun updateTransform() {
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

    override fun updateGraph(results: List<Classifier.Companion.Recognition>) {
        for (recognition in results) {
            Log.d(TAG, "Recognition(id=${recognition.id}, title=${recognition.title}, confidence=${recognition.confidence}")
            val index = emotions.indexOf(recognition.title)
            Log.d(TAG, "index: $index")
            mGraphData[index].y = recognition.confidence
        }
        chart.notifyDataSetChanged()
        chart.invalidate()
        //chart.animateY(500)
    }

    private fun setGraph() {
        val barDataset = BarDataSet(mGraphData, "").apply {
            colors = listOf(
                ContextCompat.getColor(this@CameraActivity, R.color.red),
                ContextCompat.getColor(this@CameraActivity, R.color.green),
                ContextCompat.getColor(this@CameraActivity, R.color.blue),
                ContextCompat.getColor(this@CameraActivity, R.color.yellow),
                ContextCompat.getColor(this@CameraActivity, android.R.color.white),
                ContextCompat.getColor(this@CameraActivity, R.color.gray),
                ContextCompat.getColor(this@CameraActivity, R.color.purple)
            )
        }
        //chart.animateY(5000)
        val barData = BarData(barDataset).apply {
            barWidth = 0.9f
        }

        val xAxis = chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String = emotions[value.toInt()]
            }
            textColor = ContextCompat.getColor(this@CameraActivity, android.R.color.white)
        }
        val yAxisLeft = chart.axisLeft.apply {
            axisMaximum = 1f
            axisMinimum = 0f
        }
        val yAxisRight = chart.axisRight.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        chart.description.text = ""
        chart.data = barData
        chart.setFitBars(true)
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.camera, toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = CameraActivity::class.java.simpleName
    }
}
