package com.example.githubproject

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.adapter.ListUserAdapter
import com.example.githubproject.data.api.ApiService
import com.example.githubproject.data.api.db.DatabaseHelper
import com.example.githubproject.databinding.ActivityMainBinding
import com.example.githubproject.favorite.FavoriteActivity
import com.example.githubproject.model.userData
import com.example.githubproject.profile.UserProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListUserAdapter

    companion object {
        private var TAG = MainActivity::class.java.simpleName

        const val LOGIN = "intent_login"
        const val IMAGE = "intent_image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Home"

        binding = ActivityMainBinding.inflate(layoutInflater)

        setRecycler()
        getDataFromApi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        // SEARCH
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter!!.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter!!.filter.filter(newText)
                return true
            }
        })

        //CLICK
        val favorite = menu.findItem(R.id.favorite)
        favorite.setOnMenuItemClickListener {
            startActivity(
                    Intent(applicationContext, FavoriteActivity::class.java)
            )
            return@setOnMenuItemClickListener true
        }
        return true
    }

    //RECYCLER
    private fun setRecycler() {
        listAdapter = ListUserAdapter(arrayListOf(), object : ListUserAdapter.onAdapterListener {
            override fun onClick(result: userData) {
                //Toast.makeText(applicationContext, result.login, Toast.LENGTH_SHORT).show()
                startActivity(
                        Intent(applicationContext, UserProfile::class.java)
                                .putExtra(LOGIN, result.login)
                                .putExtra(IMAGE, result.avatar_url)
                )
            }
        })
        findViewById<RecyclerView>(R.id.rv_users).apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = listAdapter
        }
    }

    private fun getDataFromApi() {

        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

        ApiService.endpoint.getUser()
                .enqueue(object : Callback<List<userData>> {
                    override fun onResponse(call: Call<List<userData>>, response: Response<List<userData>>) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        if (response.isSuccessful) {
                            showData(response.body()!!)
                        }
                    }

                    override fun onFailure(call: Call<List<userData>>, t: Throwable) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        Log.d(TAG, t.toString())
                    }
                })
    }

    private fun showData(data: List<userData>) {
        val result = data
        listAdapter.setData(result)
    }
}