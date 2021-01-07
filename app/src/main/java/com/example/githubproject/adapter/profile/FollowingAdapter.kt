package com.example.githubproject.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.adapter.ListUserAdapter
import com.example.githubproject.model.userData

class FollowingAdapter(private val listFollowing: ArrayList<userData>, val listener: onAdapterListener) : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        return FollowingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.following_recycler_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return listFollowing.size
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val following = listFollowing[position]

        holder.view.findViewById<TextView>(R.id.id_following).text = following.login

        Glide.with(holder.view)
                .load(following.avatar_url)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(holder.view.findViewById(R.id.image_following))
        holder.view.setOnClickListener{
            listener.onClick(following)
        }
    }

    class FollowingViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: List<userData>) {
        listFollowing.clear()
        listFollowing.addAll(data)
        notifyDataSetChanged()
    }

    interface onAdapterListener {
        fun onClick(result: userData)
    }
}