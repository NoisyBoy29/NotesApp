package com.naufal.notesapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.naufal.notesapp.R
import com.naufal.notesapp.databinding.ActivityCrudnoteBinding
import com.naufal.notesapp.db.DatabaseConfig
import com.naufal.notesapp.db.DatabaseConfig.NoteColumns.Companion.DATE
import com.naufal.notesapp.entity.Note
import com.naufal.notesapp.db.NoteHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CRUDNoteActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var noteHelper: NoteHelper
    private lateinit var binding: ActivityCrudnoteBinding
    private val speechRec = 102

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudnoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()
        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }
        val actionBarTitle: String
        if (isEdit) {
            actionBarTitle = "Ubah Catatan " + note?.title
            note?.let {
                binding.edtTitle.setText(it.title)
                binding.edtDescription.setText(it.description)
            }
        } else {
            actionBarTitle = "Menambahkan Catatan Baru"
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSimpan.setOnClickListener(this)
        binding.voiceSpeechButton.setOnClickListener {
            speechInput()
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_simpan) {
            val title = binding.edtTitle.text.toString().trim()
            val description = binding.edtDescription.text.toString().trim()
            if (title.isEmpty()) {
                binding.edtTitle.error = "Field can not be blank"
                return
            }
            note?.title = title
            note?.description = description
            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseConfig.NoteColumns.TITLE, title)
            values.put(DatabaseConfig.NoteColumns.DESCRIPTION, description)
            if (isEdit) {
                // Mengupdate data note yang sudah ada
                val result = noteHelper.updateById(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@CRUDNoteActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Menambahkan data note baru
                note?.date = getCurrentDate()
                values.put(DATE, getCurrentDate())
                val result = noteHelper.insertData(values)
                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@CRUDNoteActivity, "Gagal menambah data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // Mendapatkan tanggal dan waktu saat ini
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("EEEE dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            // Menampilkan menu delete jika sedang dalam mode edit
            menuInflater.inflate(R.menu.menu_note, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan operasi data ?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus catatan ini?"
            dialogTitle = "Hapus Catatan"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder.setMessage(dialogMessage).setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    // Menghapus data note
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@CRUDNoteActivity, "Gagal menghapus Catatan", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == speechRec && resultCode == Activity.RESULT_OK) {
            val result: ArrayList<String>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            binding.edtDescription.text =
                Editable.Factory.getInstance().newEditable(result?.get(0).toString())
        }
    }

    private fun speechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech Failed", Toast.LENGTH_SHORT).show()
        } else {
            val voiceSpeech = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            voiceSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            voiceSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            voiceSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
            startActivityForResult(voiceSpeech, speechRec)
        }
    }
}
