package com.example.githubproject.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class userData (
        val id: Int = 0,
        val login: String="",
        val avatar_url: String="",
        val name: String="",
        val followers: String="",
        val following: String="",
        val html_url: String=""
) : Parcelable

