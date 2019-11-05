package com.content.mercy.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.content.mercy.R
import com.content.mercy.camera.CameraActivity
import com.content.mercy.main.adapter.FeelListAdapter
import com.content.mercy.model.FeelCard
import com.content.mercy.text.TextActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val emotions = resources.getStringArray(R.array.emotions)
        val feelList = arrayListOf(
            FeelCard("2019-11-05", emotions[0], "Today is my birthday. I had a happy day with my friends.", "dog00"),
            FeelCard("2019-11-05", emotions[1], "It was so saaaaaad.\u2028I’m so depressed today.", "dog00"),
            FeelCard("2019-11-05", emotions[2], "I went to Itaewon with my boyfriend for a date. I had a great time with him.", "dog01"),
            FeelCard("2019-11-05", emotions[3], "I went to Itaewon with my boyfriend for a date. I had a great time with him.", "dog02"),
            FeelCard("2019-11-05", emotions[4], "I went to Itaewon with my boyfriend for a date. I had a great time with him.", "dog03"),
            FeelCard("2019-11-05", emotions[5], "I went to Itaewon with my boyfriend for a date. I had a great time with him.", "dog04"),
            FeelCard("2019-11-05", emotions[6], "I went to Itaewon with my boyfriend for a date. I had a great time with him.", "dog05"),
            FeelCard("2019-11-05", emotions[7], "I went to Itaewon with my boyfriend for a date. I had a great time with him.", "dog06")
        )

        rootView.mainRecycler.apply {
            adapter = FeelListAdapter(requireContext(), feelList)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        val fabs = arrayOf(
            rootView.fab_gallery,
            rootView.fab_voice,
            rootView.fab_video,
            rootView.fab_camera,
            rootView.fab_text
        ).apply { forEach { it.setOnClickListener(this@MainFragment) }}
        var isFabOpen = false

        val openAnimation = AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.fab_open)
        val closeAnimation = AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.fab_close)
        val mFab = rootView.findViewById<FloatingActionButton>(R.id.fab_main)
        mFab.setOnClickListener {
            if (isFabOpen) {
                fabs.forEachIndexed { index, floatingActionButton ->
                    floatingActionButton.startAnimation(closeAnimation)
                    floatingActionButton.isClickable = false
                }
                isFabOpen = false
            } else {
                fabs.forEachIndexed { index, floatingActionButton ->
                    floatingActionButton.startAnimation(openAnimation)
                    floatingActionButton.isClickable = true
                }
                isFabOpen = true
            }
        }

        return rootView
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab_gallery -> { }
            R.id.fab_voice -> { }
            R.id.fab_video -> { }
            R.id.fab_camera -> {
                startActivity(Intent(context, CameraActivity::class.java))
            }
            R.id.fab_text -> {
                startActivity(Intent(context, TextActivity::class.java))
            }
        }
    }
}