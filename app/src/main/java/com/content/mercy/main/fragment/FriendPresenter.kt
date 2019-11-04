package com.content.mercy.main.fragment

import com.content.mercy.model.Friend
import com.content.mercy.repository.FriendRepository

class FriendPresenter : FriendContract.Presenter {

    override fun start() {

   }

    override fun getFriends(): ArrayList<Friend> = FriendRepository.getInstance().getFriends()

    override fun showFriendDetail(position: Int) {
        val friend = getFriends()[position]
}
}