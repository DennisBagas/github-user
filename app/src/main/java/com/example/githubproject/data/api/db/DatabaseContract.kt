package com.example.githubproject.data.api.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class NoteColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "favorit"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val NAME = "name"
            const val HTML_URL = "html_url"
            const val AVATAR_URL = "avatar_url"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
        }
    }
}