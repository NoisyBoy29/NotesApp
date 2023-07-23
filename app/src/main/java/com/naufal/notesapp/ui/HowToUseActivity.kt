package com.naufal.notesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.naufal.notesapp.R

class HowToUseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_use)
        val actionbar = supportActionBar

        // Menetapkan judul action bar
        actionbar!!.title = "Panduan Aplikasi"

        // Menampilkan tombol "back" di action bar
        actionbar.setDisplayHomeAsUpEnabled(true)
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
