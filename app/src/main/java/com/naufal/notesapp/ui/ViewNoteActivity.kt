package com.naufal.notesapp.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.naufal.notesapp.R
import com.naufal.notesapp.databinding.ActivityViewNoteBinding
import com.naufal.notesapp.entity.Note

class ViewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewNoteBinding
    private var note: Note? = null
    private var items = arrayOf("English", "Indonesia")
    private var conditions = DownloadConditions.Builder().requireWifi().build()

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

        binding.copyTranslateButton.setOnClickListener {
            copyTranslateToClipboard()
        }

        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line, items
        )

        binding.languageFrom.setAdapter(itemsAdapter)

        binding.languageTo.setAdapter(itemsAdapter)

        binding.translateButton.setOnClickListener {
            val options = TranslatorOptions.Builder().setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo()).build()

            val translator = Translation.getClient(options)

            // Mendownload model terjemahan jika diperlukan
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener {

                // Melakukan terjemahan
                translator.translate(binding.edtDescription.text.toString())
                    .addOnSuccessListener { translatedText ->
                        binding.translateOutput.text = translatedText
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Cek apakah tombol back yang ditekan
                if (!isTaskRoot) {
                    // Jika bukan activity utama, kembali ke halaman sebelumnya (ViewNoteActivity)
                    finish()
                } else {
                    // Jika tombol back ditekan dan activity ini adalah activity utama, buat intent untuk kembali ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
                return true
            }

            R.id.action_edit -> {
                // Jika menu Edit diklik, pindah ke edit catatan
                val intent = Intent(this, CRUDNoteActivity::class.java)
                intent.putExtra(CRUDNoteActivity.EXTRA_NOTE, note)
                intent.putExtra(CRUDNoteActivity.EXTRA_POSITION, -1) // Tidak memerlukan posisi untuk edit
                startActivityForResult(intent, CRUDNoteActivity.REQUEST_EDIT)
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

    override fun onBackPressed() {
        if (!isTaskRoot) {
            // Jika bukan activity utama, kembali ke halaman sebelumnya (ViewNoteActivity)
            finish()
        } else {
            // Jika tombol back ditekan dan activity ini adalah activity utama, buat intent untuk kembali ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }



    // Menyalin isi deskripsi ke clipboard
    private fun copyTranslateToClipboard() {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyDescNote = binding.translateOutput.text.toString()
        val clipDesc = ClipData.newPlainText("Salin Teks", copyDescNote)
        clipboardManager.setPrimaryClip(clipDesc)
        Toast.makeText(this, "Teks berhasil disalin ke clipboard", Toast.LENGTH_SHORT).show()
    }

    // Memilih bahasa sumber terjemahan
    private fun selectFrom(): String {
        return when (binding.languageFrom.text.toString()) {
            "Pilih Bahasa" -> TranslateLanguage.ENGLISH
            "English" -> TranslateLanguage.ENGLISH
            "Indonesia" -> TranslateLanguage.INDONESIAN
            else -> TranslateLanguage.ENGLISH
        }
    }

    // Memilih bahasa tujuan terjemahan
    private fun selectTo(): String {
        return when (binding.languageTo.text.toString()) {
            "Pilih Bahasa" -> TranslateLanguage.ENGLISH
            "English" -> TranslateLanguage.ENGLISH
            "Indonesia" -> TranslateLanguage.INDONESIAN
            else -> TranslateLanguage.INDONESIAN
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CRUDNoteActivity.REQUEST_EDIT) {
            if (resultCode == CRUDNoteActivity.RESULT_UPDATE) {
                if (data != null) {
                    val updatedNote = data.getParcelableExtra<Note>(CRUDNoteActivity.EXTRA_NOTE)

                    if (updatedNote != null) {
                        note = updatedNote
                        binding.edtTitle.text = SpannableStringBuilder(note?.title)
                        binding.edtDescription.text = SpannableStringBuilder(note?.description)
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            } else if (resultCode == CRUDNoteActivity.RESULT_DELETE) {
                setResult(CRUDNoteActivity.RESULT_DELETE, data)
                finish()
            }
        }
    }
}

