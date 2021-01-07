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
import com.example.githubproject.adapter.profile.FollowingAdapter
import com.example.githubproject.data.api.ApiService
import com.example.githubproject.model.userData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowingActivity : AppCompatActivity() {

    private lateinit var following: String
    private lateinit var listFollowing: FollowingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        following = intent.getStringExtra("following").toString()

        supportActionBar?.title = "Following"

        setRecycler()
        getApiFollowing()
    }

    private fun setRecycler(){
        listFollowing = FollowingAdapter(arrayListOf(), object : FollowingAdapter.onAdapterListener{
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
            adapter = listFollowing
        }
    }

    fun getApiFollowing() {
        ApiService.endpoint.getUserProfileFollowing(following)
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
        listFollowing.setData(result)

    }
}