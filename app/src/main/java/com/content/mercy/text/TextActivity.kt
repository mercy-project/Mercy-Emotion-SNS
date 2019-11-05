package com.content.mercy.text

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.content.mercy.R
import com.content.mercy.retrofit.Repo
import com.content.mercy.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_text.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_check_24px)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.text, toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val text = diary.text.toString()
                // TODO: Progress + Room
                RetrofitClient.retrofitService.listRepos(text).enqueue(object : Callback<Repo> {
                    override fun onResponse(call: Call<Repo>, response: Response<Repo>) {
                        Log.d(TAG, "Retrofit onResponse")
                        response.body()?.let {
                            Log.d(TAG, "Response(sentiment=${it.sentiment}, sentence=${it.sentence})")
                        }
                    }

                    override fun onFailure(call: Call<Repo>, t: Throwable) {
                        call.cancel()
                        Log.e(TAG, "Retrofit onFailure..", t)
                    }
                })
            }
            R.id.undo -> { }
            R.id.redo -> { }
            R.id.text_format -> { }
            R.id.add -> { }
            R.id.more -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = TextActivity::class.java.simpleName
    }
}
