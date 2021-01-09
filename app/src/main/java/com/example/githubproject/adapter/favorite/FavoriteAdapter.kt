package com.example.githubproject.adapter.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.data.api.db.NoteHelper
import com.example.githubproject.favorite.FavoriteActivity
import com.example.githubproject.model.userData

class FavoriteAdapter(var listFavorite: ArrayList<userData>, val listener: onAdapterListener): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var isEdit = false
    private var user: userData? = null
    private var position: Int = 0
    private lateinit var noteHelper: NoteHelper
    private lateinit var context : Context

    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        noteHelper = NoteHelper.getInstance(context)
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

        holder.view.findViewById<ImageView>(R.id.delete_favorite).setOnClickListener{
            Toast.makeText(context, "Ke Klik kok", Toast.LENGTH_SHORT).show()
            val result = noteHelper.deleteById(favorite.id.toString()).toLong()
            if (result > 0) {
                removeItem(position)
            }
        }

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