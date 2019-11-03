package com.content.mercy.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.content.mercy.R
import com.content.mercy.main.adapter.FriendListAdapter
import com.content.mercy.model.User
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * Created by rapsealk on 2019-11-01..
 */
class MainFragment : Fragment(), MainContract.View {

    private var mPresenter: MainContract.Presenter = MainPresenter(this)

    override var presenter: MainContract.Presenter
        get() = mPresenter
        set(value) { mPresenter = value }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        rootView.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val friends = ArrayList<User>().apply {
                addAll(mPresenter.getFriends())
            }
            adapter = FriendListAdapter(this@MainFragment, friends).apply {
                /*
                itemClickListener = object : FriendListAdapter.ItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Log.d(TAG, "Item click: $position")
                        mPresenter.showFriendDetail(position)
                    }
                }
                */
            }
        }

        return rootView
    }
    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    */

    override fun getActivityContext(): Context = requireContext()

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}