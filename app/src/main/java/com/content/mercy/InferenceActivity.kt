package com.content.mercy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_inference.*

class InferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inference)

        videoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                setData(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                setType("video/mp4")
            }
            startActivityForResult(Intent.createChooser(intent, "Video.."), 0x0001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x0001) {
                val uri = data?.data
                Log.d(TAG, "Uri: $uri")

                var path = "PATH"
                val project = arrayOf(MediaStore.Video.Media.DATA)
                val cursor = contentResolver.query(uri!!, project, null, null, null)?.apply {
                    val column_index = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    moveToFirst()
                    path = getString(column_index)
                    close()
                }
                Log.d(TAG, "Path: $path")
            }
        }
    }

    companion object {
        private val TAG = InferenceActivity::class.java.simpleName
    }
}
