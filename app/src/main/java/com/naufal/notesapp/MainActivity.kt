package com.naufal.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.naufal.notesapp.databinding.ActivityMainBinding
import com.naufal.notesapp.adapter.NoteAdapter
import com.naufal.notesapp.db.Note
import com.naufal.notesapp.db.NoteHelper
import com.naufal.notesapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter

    // Init launcher untuk memulai aktivitas dengan hasil
    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                when (result.resultCode) {
                    // Akan dipanggil jika request codenya ADD
                    CRUDNoteActivity.RESULT_ADD -> {
                        val note =
                            result.data?.getParcelableExtra<Note>(CRUDNoteActivity.EXTRA_NOTE) as Note
                        adapter.addItem(note)
                        binding.rvNotes.smoothScrollToPosition(adapter.itemCount - 1)
                        showSnackbarMessage("Satu item berhasil ditambahkan")
                    }
                    // Akan dipanggil jika request codenya UPDATE
                    CRUDNoteActivity.RESULT_UPDATE -> {
                        val note =
                            result.data?.getParcelableExtra<Note>(CRUDNoteActivity.EXTRA_NOTE) as Note
                        val position =
                            result?.data?.getIntExtra(CRUDNoteActivity.EXTRA_POSITION, 0) as Int
                        adapter.updateItem(position, note)
                        binding.rvNotes.smoothScrollToPosition(position)
                        showSnackbarMessage("Satu item berhasil diubah")
                    }
                    // Akan dipanggil jika request codenya DELETE
                    CRUDNoteActivity.RESULT_DELETE -> {
                        val position =
                            result?.data?.getIntExtra(CRUDNoteActivity.EXTRA_POSITION, 0) as Int
                        adapter.removeItem(position)
                        showSnackbarMessage("Satu item berhasil dihapus")
                    }
                }
            }
        }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Notes"

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.setHasFixedSize(true)

        // Inisialisasi adapter
        adapter = NoteAdapter(object : NoteAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedNote: Note?, position: Int?) {
                val intent = Intent(this@MainActivity, CRUDNoteActivity::class.java)
                intent.putExtra(CRUDNoteActivity.EXTRA_NOTE, selectedNote)
                intent.putExtra(CRUDNoteActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })
        binding.rvNotes.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, CRUDNoteActivity::class.java)
            resultLauncher.launch(intent)
        }

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
                // Menampilkan data lama langsung pada RecyclerView
                adapter.notifyDataSetChanged()
            } else {
                // Jika tidak ada data di savedInstanceState, load data seperti biasa
                loadNotesAsync()
            }
        }
    }

    // loading data secara asynchronous
    private fun loadNotesAsync() {
        lifecycleScope.launch {
            binding.progressbar.visibility = View.VISIBLE
            val noteHelper = NoteHelper.getInstance(applicationContext)
            noteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressbar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            noteHelper.close()

            // Menampilkan data langsung pada RecyclerView
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    // Menampilkan pesan snackbar
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvNotes, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_how -> {
                val howToUse = Intent(this@MainActivity, HowToUseActivity::class.java)
                startActivity(howToUse)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

