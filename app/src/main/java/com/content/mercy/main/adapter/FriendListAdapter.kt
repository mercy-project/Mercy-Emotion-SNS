package com.content.mercy.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.content.mercy.R
import com.content.mercy.model.User
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by rapsealk on 2019-11-02..
 */
class FriendListAdapter(private val mItems: ArrayList<User>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        mItems.addAll(0, arrayListOf(
            User(UUID.randomUUID().toString(), "사용자"),
            User(UUID.randomUUID().toString(), "친구")
        ))
    }

    enum class ViewType(type: Int) {
        HEADER(0), USER(1)
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private var mItemClickListener: ItemClickListener? = null
    var itemClickListener: ItemClickListener?
        get() = mItemClickListener
        set(value) { mItemClickListener = value }

    override fun getItemCount(): Int = mItems.size

    override fun getItemViewType(position: Int): Int = when (position) {
        HEADER_POSITION -> ViewType.HEADER.ordinal
        else -> ViewType.USER.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = when (viewType) {
            ViewType.HEADER.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                UserViewHolder(view)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItems[position]
        if (position == HEADER_POSITION) {
            holder as HeaderViewHolder
            holder.headerName.text = item.name
        } else {
            holder as UserViewHolder
            holder.name.text = item.name
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerName: TextView = view.findViewById(R.id.headerName)
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
    }

    companion object {
        private val TAG = FriendListAdapter::class.java.simpleName

        private const val HEADER_POSITION = 1
    }
}