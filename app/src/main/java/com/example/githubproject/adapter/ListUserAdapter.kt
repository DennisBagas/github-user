package com.example.githubproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.model.userData

class ListUserAdapter(private var listUser: ArrayList<userData>, val listener: onAdapterListener) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(), Filterable {

    private var listUserFilter = listUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user_home, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.view.findViewById<TextView>(R.id.id_user).text = user.login
        holder.view.findViewById<TextView>(R.id.url_user).text = user.html_url

        Glide.with(holder.view)
                .load(user.avatar_url)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(holder.view.findViewById(R.id.image_user))
        holder.view.setOnClickListener{
            listener.onClick(user)
        }
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filter = FilterResults()

                if(constraint == null || constraint.length < 0) {
                    filter.count = listUserFilter.size
                    filter.values = listUserFilter
                } else {
                    var searchChar = constraint.toString().toLowerCase()
                    val itemModel = ArrayList<userData>()

                    for(item in listUserFilter) {
                        if(item.login.contains(searchChar)){
                            itemModel.add(item)
                        }
                    }
                    filter.count = itemModel.size
                    filter.values = itemModel
                }
                return filter
            }

            override fun publishResults(constraint: CharSequence?, filter: FilterResults?) {
                listUser = filter!!.values as ArrayList<userData>
                notifyDataSetChanged()
            }
        }
    }


    fun setData(data: List<userData>) {
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }

    interface onAdapterListener {
        fun onClick(result: userData)
    }
}