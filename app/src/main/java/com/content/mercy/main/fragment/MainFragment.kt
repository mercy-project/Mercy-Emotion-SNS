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
            FeelCard("Welsh Corgi", emotions[0], "0", "dog00"),
            FeelCard("Chow Chow", emotions[1], "4", "dog00"),
            FeelCard("Breed Pomeranian", emotions[2], "1", "dog01"),
            FeelCard("Golden Retriver", emotions[3], "3", "dog02"),
            FeelCard("Yorkshire Terrier", emotions[4], "5", "dog03"),
            FeelCard("Pug", emotions[5], "6", "dog04"),
            FeelCard("Alaskan Malamute", emotions[6], "7", "dog05"),
            FeelCard("Shih Tzu", emotions[7], "5", "dog06")
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