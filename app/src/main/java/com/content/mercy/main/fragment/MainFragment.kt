package com.content.mercy.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.content.mercy.R
import com.content.mercy.main.adapter.FeelListAdapter
import com.content.mercy.model.FeelCard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment(){

var feelList= arrayListOf<FeelCard>(
    FeelCard("Chow Chow", "Male", "4", "dog00"),
    FeelCard("Breed Pomeranian", "Female", "1", "dog01"),
    FeelCard("Golden Retriver", "Female", "3", "dog02"),
    FeelCard("Yorkshire Terrier", "Male", "5", "dog03"),
    FeelCard("Pug", "Male", "4", "dog04"),
    FeelCard("Alaskan Malamute", "Male", "7", "dog05"),
    FeelCard("Shih Tzu", "Female", "5", "dog06")
)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        rootView.mainRecycler.apply {
            adapter = FeelListAdapter(requireContext(), feelList)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        val mFab = rootView.findViewById<FloatingActionButton>(R.id.fab_main)
        mFab.setOnClickListener {
            Toast.makeText(context, "click", Toast.LENGTH_LONG).show()
        }

        return rootView
    }
}