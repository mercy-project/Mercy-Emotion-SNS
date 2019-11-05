package com.content.mercy.main.adapter

import android.content.Context
import android.util.Log
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
            val emotions = context.resources.getStringArray(R.array.emotions)
            val emoticons = arrayOf(
                R.drawable.happy,
                R.drawable.sad,
                R.drawable.neutral,
                R.drawable.contempt,
                R.drawable.fear,
                R.drawable.dislike,
                R.drawable.surprised,
                R.drawable.angry
            )
            Log.d(TAG, "emotions: ${emotions.joinToString(", ")}")
            Log.d(TAG, "emoticons: ${emoticons.joinToString(", ")}")
            val id = emotions.indexOf(feel.Feeling)
            Log.d(TAG, "emotion: ${feel.Feeling}, index: $id")
            if (id > -1) {
                val drawable = emoticons[id]
                Log.d(TAG, "emoticon: $drawable")
                image.setImageDrawable(context.resources.getDrawable(drawable, context.theme))
            }
            //image.setImageResource(emoticons[emotions.indexOf(feel.Feeling)])
            /*
            if (feel.image != "") {
                val resourceId = context.resources.getIdentifier(feel.image.toLowerCase(Locale.getDefault()), "drawable", context.packageName)
                image?.setImageResource(resourceId)
            } else {
                image?.setImageResource(R.mipmap.ic_launcher)
            }
            */
            Date?.text = feel.Date
            Feeling?.text = feel.Feeling
            Description?.text = feel.Description
        }
    }

    companion object {
        private val TAG = FeelListAdapter::class.java.simpleName
    }
}