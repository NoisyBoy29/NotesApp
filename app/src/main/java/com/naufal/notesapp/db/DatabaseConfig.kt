package com.naufal.notesapp.db

import android.provider.BaseColumns

internal class DatabaseConfig {

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "note"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"

        }
    }
}