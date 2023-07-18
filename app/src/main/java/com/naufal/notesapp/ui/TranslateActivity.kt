package com.naufal.notesapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.naufal.notesapp.R
import com.naufal.notesapp.databinding.ActivityTranslateBinding
import java.util.Locale

class TranslateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTranslateBinding
    private var items = arrayOf("English", "Indonesia")
    private var conditions = DownloadConditions.Builder().requireWifi().build()
    private val speechRec = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_translate)
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
                translator.translate(binding.tvInput.text.toString())
                    .addOnSuccessListener { translatedText ->
                        binding.tvOutput.text = translatedText
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.voiceSpeechButton.setOnClickListener {
            speechInput()
        }
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

        if (requestCode == speechRec && resultCode == Activity.RESULT_OK) {
            val result: ArrayList<String>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            binding.tvInput.text =
                Editable.Factory.getInstance().newEditable(result?.get(0).toString())
        }
    }

    // Mengaktifkan input suara
    private fun speechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech Failed", Toast.LENGTH_SHORT).show()
        } else {
            val voiceSpeech = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            voiceSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            voiceSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            voiceSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
            startActivityForResult(voiceSpeech, speechRec)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Menghandle klik tombol "back" di action bar
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // Kembali ke MainActivity saat tombol back ditekan
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}