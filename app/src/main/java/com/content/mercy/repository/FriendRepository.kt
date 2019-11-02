package com.content.mercy.repository

import com.content.mercy.model.Friend
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by rapsealk on 2019-11-03..
 */
class FriendRepository {

    // FIXME
    private var data = arrayListOf(
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름"),
        Friend(UUID.randomUUID().toString(), "사용자 이름")
    )
    //private val mDatabase

    fun getFriends(): ArrayList<Friend> = data

    companion object {
        private val TAG = FriendRepository::class.java.simpleName

        private val repository by lazy { FriendRepository() }

        fun getInstance() = repository
    }
}