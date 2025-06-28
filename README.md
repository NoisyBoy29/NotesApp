# NotesApp

**ENGLISH VERSION BELOW**

---

## 🇮🇩 Bahasa Indonesia

**NotesApp** adalah aplikasi catatan sederhana berbasis Android yang menggunakan bahasa pemrograman Kotlin. Aplikasi ini memungkinkan pengguna untuk membuat, mengedit, menghapus, dan menerjemahkan catatan. NotesApp juga mendukung input suara dan seluruh data catatan disimpan secara lokal menggunakan SQLite.

### ✨ Fitur Utama

- **CRUD Catatan:** Tambah, edit, dan hapus catatan dengan mudah.
- **Input Suara:** Isi deskripsi catatan dengan fitur Speech-to-Text.
- **Terjemahan Catatan:** Terjemahkan isi catatan antara Bahasa Indonesia dan Inggris menggunakan Google ML Kit Translation.
- **Manajemen Data Lokal:** Semua data catatan disimpan di SQLite Database.
- **Antarmuka Modern:** Daftar catatan tampil dinamis dengan RecyclerView.
- **Copy & Translate:** Salin hasil terjemahan langsung ke clipboard.

### 🗂️ Struktur Proyek

```
app/
└── src/
    └── main/
        └── java/
            └── com/naufal/notesapp/
                ├── adapter/         # NoteAdapter untuk RecyclerView
                ├── db/              # DatabaseHelper & NoteHelper untuk SQLite
                ├── entity/          # Data class Note
                ├── helper/          # MappingHelper untuk konversi Cursor ke ArrayList
                └── ui/              # Activity utama (Main, CRUD, View)
```

**Penjelasan Folder & File Penting:**
- `adapter/NoteAdapter.kt` – Adapter RecyclerView untuk catatan.
- `db/NoteHelper.kt & DatabaseHelper.kt` – Manajemen database (insert, update, delete, query).
- `entity/Note.kt` – Model data catatan.
- `ui/MainActivity.kt` – Halaman utama menampilkan daftar catatan.
- `ui/CRUDNoteActivity.kt` – Tambah & edit catatan (termasuk input suara).
- `ui/ViewNoteActivity.kt` – Detail & terjemahan catatan.
- `helper/MappingHelper.kt` – Konversi data database ke objek Note.

### 🚀 Instalasi & Menjalankan Proyek

1. **Clone repository:**
   ```bash
   git clone https://github.com/NoisyBoy29/NotesApp.git
   ```
2. **Buka di Android Studio.**
3. **Build & Run** pada emulator atau perangkat Android.

### 📦 Dependencies

- Kotlin
- AndroidX
- Google ML Kit Translation
- RecyclerView

> **Catatan:**  
> Proyek ini dibuat sebagai bahan untuk Penulisan Ilmiah (PI).

---

## 🇬🇧 English

**NotesApp** is a simple note-taking application for Android, built using Kotlin. This app lets users create, update, delete, and translate notes. It supports voice input and stores all notes locally using SQLite.

### ✨ Main Features

- **CRUD Notes:** Easily add, edit, and delete notes.
- **Voice Input:** Fill in note descriptions using Speech-to-Text.
- **Note Translation:** Translate notes between Indonesian and English using Google ML Kit Translation.
- **Local Data Management:** All notes are stored locally using SQLite Database.
- **Modern UI:** Dynamic note list with RecyclerView.
- **Copy & Translate:** Copy translated results directly to clipboard.

### 🗂️ Project Structure

```
app/
└── src/
    └── main/
        └── java/
            └── com/naufal/notesapp/
                ├── adapter/         # NoteAdapter for RecyclerView
                ├── db/              # DatabaseHelper & NoteHelper for SQLite
                ├── entity/          # Note data class
                ├── helper/          # MappingHelper for Cursor to ArrayList conversion
                └── ui/              # Main, CRUD, and View Activities
```

**Key Files & Folders:**
- `adapter/NoteAdapter.kt` – RecyclerView adapter for displaying notes.
- `db/NoteHelper.kt & DatabaseHelper.kt` – Manages database operations (insert, update, delete, query).
- `entity/Note.kt` – Data model for notes.
- `ui/MainActivity.kt` – Main screen showing the note list.
- `ui/CRUDNoteActivity.kt` – Add & edit notes (with voice input).
- `ui/ViewNoteActivity.kt` – View details & translate notes.
- `helper/MappingHelper.kt` – Helper to convert database data to Note objects.

### 🚀 Installation & Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/NoisyBoy29/NotesApp.git
   ```
2. **Open in Android Studio.**
3. **Build & Run** on an emulator or Android device.

### 📦 Dependencies

- Kotlin
- AndroidX
- Google ML Kit Translation
- RecyclerView

> **Note:**  
> This project was created for a Scientific Writing (Penulisan Ilmiah) assignment.

---
