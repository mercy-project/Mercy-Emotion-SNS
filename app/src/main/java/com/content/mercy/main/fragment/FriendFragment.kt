
package com.content.mercy.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.content.mercy.R
import com.content.mercy.main.adapter.FriendListAdapter
import com.content.mercy.model.User
import kotlinx.android.synthetic.main.activity_friend_fragment.view.*

class FriendFragment : Fragment(), FriendContract.View {

    private var mPresenter: FriendContract.Presenter =
        FriendPresenter()

    override var presenter: FriendContract.Presenter
        get() = mPresenter
        set(value) { mPresenter = value }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_friend_fragment, container, false)

        rootView.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val friends = ArrayList<User>().apply {
                addAll(mPresenter.getFriends())
            }
            adapter = FriendListAdapter(friends).apply {
                itemClickListener = object : FriendListAdapter.ItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        mPresenter.showFriendDetail(position)
                    }
                }
            }
        }

        return rootView
    }
}