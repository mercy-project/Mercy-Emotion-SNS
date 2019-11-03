package com.content.mercy.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.content.mercy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val mFab = rootView.findViewById<FloatingActionButton>(R.id.fab_main)
        mFab.setOnClickListener {
            Toast.makeText(context, "click", Toast.LENGTH_LONG).show()
        }



        return rootView
    }
}