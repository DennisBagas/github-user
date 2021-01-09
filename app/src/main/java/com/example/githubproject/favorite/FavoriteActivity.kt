package com.example.githubproject.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.MainActivity
import com.example.githubproject.R
import com.example.githubproject.adapter.favorite.FavoriteAdapter
import com.example.githubproject.data.api.db.NoteHelper
import com.example.githubproject.data.api.db.helper.MappingHelper
import com.example.githubproject.databinding.ActivityFavoriteBinding
import com.example.githubproject.model.userData
import com.example.githubproject.profile.UserProfile
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var noteHelper: NoteHelper

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var favorite: ArrayList<userData>

    companion object {
        private const val EXTRA_USER = "EXTRA_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<userData>(EXTRA_USER)
            if(list !== null) {
                favoriteAdapter.listFavorite = list
            }
        }

        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        RecyclerFavorite()
    }

    fun RecyclerFavorite() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter(arrayListOf(), object : FavoriteAdapter.onAdapterListener{
            override fun onClick(result: userData) {
                startActivity(
                        Intent(applicationContext, UserProfile::class.java)
                                .putExtra(MainActivity.LOGIN, result.login)
                                .putExtra(MainActivity.IMAGE, result.avatar_url)
                )}
        })
        binding.rvFavorite.adapter = favoriteAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_USER, favoriteAdapter.listFavorite)
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.GONE
            favorite = deferredFavorite.await()
            if(favorite.size > 0) {
                favoriteAdapter.setData(favorite)
            } else {
                favoriteAdapter.listFavorite = ArrayList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

//    class CustomOnItemClickListener(private val position: Int, private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {
//        override fun onClick(view: View) {
//            onItemClickCallback.onItemClicked(view, position)
//        }
//        interface OnItemClickCallback {
//            fun onItemClicked(view: View, position: Int)
//        }
//    }
}