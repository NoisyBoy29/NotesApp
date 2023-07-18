package com.naufal.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import com.naufal.notesapp.databinding.ActivityViewNoteBinding
import com.naufal.notesapp.db.Note

class ViewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewNoteBinding
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            note = intent.getParcelableExtra("EXTRA_NOTE")
            if (note != null) {
                supportActionBar?.title = "Detail Catatan " + note?.title
                binding.edtTitle.text = SpannableStringBuilder(note?.title)
                binding.edtDescription.text = SpannableStringBuilder(note?.description)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_edit -> {
                val intent = Intent()
                intent.putExtra("ACTION", "EDIT")
                intent.putExtra("NOTE", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
            R.id.action_delete -> {
                val intent = Intent()
                intent.putExtra("ACTION", "DELETE")
                intent.putExtra("NOTE", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
