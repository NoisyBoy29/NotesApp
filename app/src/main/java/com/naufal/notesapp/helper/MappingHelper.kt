package com.naufal.notesapp.helper

import android.database.Cursor
import com.naufal.notesapp.db.DatabaseConfig
import com.naufal.notesapp.entity.Note

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseConfig.NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseConfig.NoteColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseConfig.NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseConfig.NoteColumns.DATE))
                notesList.add(Note(id, title, description, date))
            }
        }
        return notesList
    }
}