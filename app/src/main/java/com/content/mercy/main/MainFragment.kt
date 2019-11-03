package com.content.mercy.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.content.mercy.R
import com.content.mercy.main.adapter.FriendListAdapter
import com.content.mercy.model.User
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        return rootView
    }
}