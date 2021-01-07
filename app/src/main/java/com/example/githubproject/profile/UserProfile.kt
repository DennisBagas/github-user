package com.example.githubproject.profile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private lateinit var login: String
    private lateinit var avatar: String
    private val TAG = UserProfile::class.java.simpleName
    private lateinit var noteHelper: NoteHelper

    //RESPONSE
    private lateinit var name: String
    private lateinit var url: String
    private lateinit var followers: String
    private lateinit var following: String

    companion object {
        const val USER_DATA = "user_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        login = intent.getStringExtra(MainActivity.LOGIN).toString()
        avatar = intent.getStringExtra(MainActivity.IMAGE).toString()

        supportActionBar?.title = "Profile " + login

        val button = findViewById<ToggleButton>(R.id.btn_toggle)

        /*
        val profilePagerAdapter = ProfileSectionAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = profilePagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f
        */

        noteHelper = NoteHelper.getInstance(applicationContext)

        getProfileApi()

        findViewById<LinearLayout>(R.id.followers_linear_profile).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.following_linear_profile).setOnClickListener(this)

        button.setOnCheckedChangeListener{ compoundButton, isChecked ->
                if(isChecked) {
                    noteHelper.open()


//                Toast.makeText(this, "Toggle On", Toast.LENGTH_SHORT).show()
//                findViewById<TextView>(R.id.name_profile).text = response.body()?.name
//                findViewById<TextView>(R.id.url_profile).text = response.body()?.html_url
//                findViewById<TextView>(R.id.followers_profile).text = response.body()?.followers
//                findViewById<TextView>(R.id.following_profile).text = response.body()?.following

                    val values = ContentValues()
                    values.put(DatabaseContract.NoteColumns.LOGIN, login)
                    values.put(DatabaseContract.NoteColumns.AVATAR, avatar)
                    values.put(DatabaseContract.NoteColumns.NAME, name)
                    values.put(DatabaseContract.NoteColumns.FOLLOWERS, followers)
                    values.put(DatabaseContract.NoteColumns.FOLLOWING, following)

                    val result = noteHelper.insert(values)

                    if (result > 0) {
                        Toast.makeText(this, "Sukses Menambah Favorit", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "GAGALLLLL", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Toggle Off", Toast.LENGTH_SHORT).show()
                }
            }
        }



    private fun getProfileApi() {

        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

        ApiService.endpoint.getUserProfile(login)
            .enqueue(object : Callback<userData> {
                override fun onResponse(call: Call<userData>, response: Response<userData>) {
                    if (response.isSuccessful) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                        name = response.body()?.name.toString()
                        url = response.body()?.html_url.toString()
                        followers = response.body()?.followers.toString()
                        following = response.body()?.following.toString()

                        findViewById<TextView>(R.id.name_profile).text = name
                        findViewById<TextView>(R.id.id_profile).text = login
                        findViewById<TextView>(R.id.url_profile).text = url
                        findViewById<TextView>(R.id.followers_profile).text = followers
                        findViewById<TextView>(R.id.following_profile).text = following

                        Glide.with(applicationContext)
                                .load(avatar)
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

//    fun onCustomToggleClick() {
//
//        val button = findViewById<ToggleButton>(R.id.btn_toggle)
//
//        button.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked) {
//                noteHelper.open()
//
//
////                Toast.makeText(this, "Toggle On", Toast.LENGTH_SHORT).show()
////                findViewById<TextView>(R.id.name_profile).text = response.body()?.name
////                findViewById<TextView>(R.id.url_profile).text = response.body()?.html_url
////                findViewById<TextView>(R.id.followers_profile).text = response.body()?.followers
////                findViewById<TextView>(R.id.following_profile).text = response.body()?.following
//
//                val values = ContentValues()
//                values.put(DatabaseContract.NoteColumns.LOGIN, login)
//                values.put(DatabaseContract.NoteColumns.AVATAR, avatar)
//                values.put(DatabaseContract.NoteColumns.NAME, name)
//                values.put(DatabaseContract.NoteColumns.FOLLOWERS, followers)
//                values.put(DatabaseContract.NoteColumns.FOLLOWING, following)
//
//                noteHelper.insert(values)
//
//            } else {
//                Toast.makeText(this,"Toggle Off", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.followers_linear_profile) {
            startActivity(
                Intent(applicationContext, FollowersActivity::class.java)
                    .putExtra("followers", login)
            )
        }
        else if(v?.id == R.id.following_linear_profile) {
            startActivity(
                Intent(applicationContext, FollowingActivity::class.java)
                    .putExtra("following", login)
            )
        }
    }
}