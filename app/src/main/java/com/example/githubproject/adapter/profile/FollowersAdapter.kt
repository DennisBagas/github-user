package com.example.githubproject.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.model.userData

class FollowersAdapter(private val listFollowers: ArrayList<userData>, val listener: onAdapterListener) : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        return FollowersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.follower_recycler_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return listFollowers.size
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        val followers = listFollowers[position]

        holder.view.findViewById<TextView>(R.id.id_followers).text = followers.login

        Glide.with(holder.view)
                .load(followers.avatar_url)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(holder.view.findViewById(R.id.image_followers))
        holder.view.setOnClickListener{
            listener.onClick(followers)
        }
    }

    class FollowersViewHolder (val view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: List<userData>) {
        listFollowers.clear()
        listFollowers.addAll(data)
        notifyDataSetChanged()
    }

    interface onAdapterListener {
        fun onClick(result: userData)
    }}