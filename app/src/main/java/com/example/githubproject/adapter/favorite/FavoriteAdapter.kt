package com.example.githubproject.adapter.favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.MainActivity
import com.example.githubproject.R
import com.example.githubproject.databinding.FavoriteUserAdapterBinding
import com.example.githubproject.favorite.FavoriteActivity
import com.example.githubproject.model.userData
import com.example.githubproject.profile.UserProfile

class FavoriteAdapter(var listFavorite: ArrayList<userData>, val listener: onAdapterListener): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {



    /*
        set(listFavorite){
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }
         */

    fun addItem(user: userData) {
        this.listFavorite.add(user)
        notifyItemInserted(this.listFavorite.size - 1)
    }

    fun updateItem(position: Int, note: userData) {
        this.listFavorite[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_user_adapter, parent, false))
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
         val favorite = listFavorite[position]

        holder.view.findViewById<TextView>(R.id.id_favorite).text = favorite.login
        holder.view.findViewById<TextView>(R.id.url_favorite).text = favorite.html_url

        Glide.with(holder.view)
                .load(favorite.avatar_url)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(holder.view.findViewById(R.id.image_favorite))

        //CLICK LISTENER FAVORITE
        holder.view.setOnClickListener{
            listener.onClick(favorite)
        }
    }

    class FavoriteViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: List<userData>) {
        if (listFavorite.size > 0) {
            this.listFavorite.clear()
        }
        this.listFavorite.addAll(data)

        notifyDataSetChanged()
    }


    interface onAdapterListener {
        fun onClick(result: userData)
    }}