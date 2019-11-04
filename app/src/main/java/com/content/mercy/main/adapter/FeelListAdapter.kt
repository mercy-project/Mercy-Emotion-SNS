package com.content.mercy.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.content.mercy.R
import com.content.mercy.model.FeelCard

class FeelListAdapter (val context: Context, val feelList: ArrayList<FeelCard>) : RecyclerView.Adapter<FeelListAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_home_feel, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(feelList[position], context)
    }

    override fun getItemCount(): Int {
        return feelList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Date = itemView.findViewById<TextView>(R.id.date)
        val Feeling = itemView.findViewById<TextView>(R.id.feel)
        val Description = itemView.findViewById<TextView>(R.id.title)
        val image = itemView.findViewById<ImageView>(R.id.feel_image)

        fun bind (feel: FeelCard, context: Context) {

            if (feel.image != "") {
                val resourceId = context.resources.getIdentifier(feel.image, "drawable", context.packageName)
                image?.setImageResource(resourceId)
            } else {
                image?.setImageResource(R.mipmap.ic_launcher)
            }
            Date?.text = feel.Date
            Feeling?.text = feel.Feeling
            Description?.text = feel.Description
        }
    }

}