package com.example.githubproject.profile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.githubproject.MainActivity
import com.example.githubproject.R
import com.example.githubproject.data.api.ApiService
import com.example.githubproject.data.api.db.DatabaseContract
import com.example.githubproject.data.api.db.NoteHelper
import com.example.githubproject.model.userData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfile : AppCompatActivity(), View.OnClickListener {


    private val TAG = UserProfile::class.java.simpleName
    private lateinit var noteHelper: NoteHelper

    //RESPONSE
    private lateinit var login: String
    private lateinit var avatar_url: String
    private lateinit var name: String
    private lateinit var html_url: String
    private lateinit var followers: String
    private lateinit var following: String

    companion object {
        const val USER_DATA = "user_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        login = intent.getStringExtra(MainActivity.LOGIN).toString()
        avatar_url = intent.getStringExtra(MainActivity.IMAGE).toString()

        supportActionBar?.title = "Profile " + login

        val button = findViewById<ToggleButton>(R.id.btn_toggle)

        noteHelper = NoteHelper.getInstance(applicationContext)

        getProfileApi()

        findViewById<LinearLayout>(R.id.followers_linear_profile).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.following_linear_profile).setOnClickListener(this)

        button.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                noteHelper.open()

                val values = ContentValues()
                values.put(DatabaseContract.NoteColumns.LOGIN, login)
                values.put(DatabaseContract.NoteColumns.AVATAR_URL, avatar_url)
                values.put(DatabaseContract.NoteColumns.NAME, name)
                values.put(DatabaseContract.NoteColumns.HTML_URL, html_url)
                values.put(DatabaseContract.NoteColumns.FOLLOWERS, followers)
                values.put(DatabaseContract.NoteColumns.FOLLOWING, following)

                val result = noteHelper.insert(values)

                if (result > 0) {
                    Toast.makeText(this, "Sukses Menambah Favorit", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "GAGALLLLL", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Toggle Off", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.to_home_menu, menu)

        val to_home = menu.findItem(R.id.to_home)

        to_home.setOnMenuItemClickListener {
            startActivity(
                    Intent(applicationContext, MainActivity::class.java)
            )
            return@setOnMenuItemClickListener true
        }
        return true
    }


    private fun getProfileApi() {

        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

        ApiService.endpoint.getUserProfile(login)
                .enqueue(object : Callback<userData> {
                    override fun onResponse(call: Call<userData>, response: Response<userData>) {
                        if (response.isSuccessful) {
                            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                            name = response.body()?.name.toString()
                            html_url = response.body()?.html_url.toString()
                            followers = response.body()?.followers.toString()
                            following = response.body()?.following.toString()

                            findViewById<TextView>(R.id.name_profile).text = name
                            findViewById<TextView>(R.id.id_profile).text = login
                            findViewById<TextView>(R.id.url_profile).text = html_url
                            findViewById<TextView>(R.id.followers_profile).text = followers
                            findViewById<TextView>(R.id.following_profile).text = following

                            Glide.with(applicationContext)
                                    .load(avatar_url)
                                    .placeholder(R.drawable.img_placeholder)
                                    .error(R.drawable.img_placeholder)
                                    .into(findViewById(R.id.image_profile))

                        }
                    }

                    override fun onFailure(call: Call<userData>, t: Throwable) {
                        Log.d(TAG, t.toString())
                    }
                })
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.followers_linear_profile) {
            startActivity(
                    Intent(applicationContext, FollowersActivity::class.java)
                            .putExtra("followers", login)
            )
        } else if (v?.id == R.id.following_linear_profile) {
            startActivity(
                    Intent(applicationContext, FollowingActivity::class.java)
                            .putExtra("following", login)
            )
        }
    }
}