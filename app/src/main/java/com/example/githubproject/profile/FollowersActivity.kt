package com.example.githubproject.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.R
import com.example.githubproject.adapter.ListUserAdapter
import com.example.githubproject.adapter.profile.FollowersAdapter
import com.example.githubproject.adapter.profile.FollowingAdapter
import com.example.githubproject.data.api.ApiService
import com.example.githubproject.model.userData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowersActivity : AppCompatActivity() {

    private lateinit var followers: String
    private lateinit var listFollowers: FollowersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        followers = intent.getStringExtra("followers").toString()

        supportActionBar?.title = "Followers"


        setRecycler()
        getApiFollowers()
    }

    private fun setRecycler(){
        listFollowers = FollowersAdapter(arrayListOf(), object : FollowersAdapter.onAdapterListener{
            override fun onClick(result: userData) {
                //Toast.makeText(applicationContext, result.login, Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(applicationContext, UserProfile::class.java)
                        .putExtra("intent_login", result.login)
                        .putExtra("intent_image", result.avatar_url)
                )
            }
        })
        findViewById<RecyclerView>(R.id.rv_following).apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = listFollowers
        }
    }

    fun getApiFollowers() {
        ApiService.endpoint.getUserProfileFollowers(followers)
            .enqueue( object : Callback<List<userData>> {
                override fun onResponse(
                    call: Call<List<userData>>,
                    response: Response<List<userData>>
                ) {
                    if(response.isSuccessful){
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        Log.d("Followers Fragment", response.body().toString())
                        showData(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<userData>>, t: Throwable) {
                    Log.d("Followers Fragment", "Hmm ternyata ada error followers")
                }
            })
    }

    private fun showData(data: List<userData>) {
        val result = data
        listFollowers.setData(result)

    }
}