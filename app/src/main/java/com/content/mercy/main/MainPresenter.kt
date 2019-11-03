package com.content.mercy.main

import com.content.mercy.model.Friend
import com.content.mercy.repository.FriendRepository

/**
 * Created by rapsealk on 2019-11-01..
 */
class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    override fun start() {

    }

    override fun getFriends(): ArrayList<Friend> = FriendRepository.getInstance().getFriends()

    override fun showFriendDetail(position: Int) {
        val friend = getFriends()[position]
    }
}