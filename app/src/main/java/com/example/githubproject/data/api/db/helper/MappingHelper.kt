package com.example.githubproject.data.api.db.helper

import android.database.Cursor
import com.example.githubproject.data.api.db.DatabaseContract
import com.example.githubproject.model.userData

object MappingHelper {

    fun mapCursorToArrayList(notesCursors: Cursor?): ArrayList<userData> {
        val notesList = ArrayList<userData>()

        notesCursors?.apply {
            while(moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.LOGIN))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.NAME))
                val html_url = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.HTML_URL))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.AVATAR_URL))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.FOLLOWING))

                notesList.add(userData(id, login, avatar_url, name, followers, following, html_url))
            }
        }
        return notesList
    }
}