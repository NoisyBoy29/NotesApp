# NotesApp

Aplikasi **NotesApp** adalah aplikasi catatan sederhana berbasis Android yang dibuat menggunakan bahasa pemrograman Kotlin. Proyek ini bertujuan untuk membantu pengguna menyimpan, mengelola, dan menerjemahkan catatan dengan fitur tambahan seperti input suara dan manajemen data lokal menggunakan SQLite.

## Fitur Utama

- **CRUD Catatan:** Tambah, ubah, dan hapus catatan.
- **Input Suara:** Mendukung pengisian deskripsi catatan dengan suara (Speech-to-Text).
- **Terjemahan Catatan:** Menerjemahkan isi catatan antara Bahasa Indonesia dan Bahasa Inggris menggunakan Google ML Kit Translation.
- **Manajemen Data Lokal:** Semua catatan disimpan secara lokal menggunakan database SQLite.
- **Antarmuka Modern:** Menggunakan RecyclerView untuk menampilkan daftar catatan secara dinamis.
- **Copy & Translate:** Salin hasil terjemahan langsung ke clipboard.

## Struktur Proyek

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

### Penjelasan Folder & File Penting

- **adapter/NoteAdapter.kt**  
  Adapter untuk menampilkan catatan pada RecyclerView.

- **db/NoteHelper.kt & DatabaseHelper.kt**  
  Manajemen operasi database (insert, update, delete, query).

- **entity/Note.kt**  
  Model data untuk catatan.

- **ui/MainActivity.kt**  
  Halaman utama yang menampilkan daftar catatan.

- **ui/CRUDNoteActivity.kt**  
  Untuk tambah & edit catatan, mendukung input suara.

- **ui/ViewNoteActivity.kt**  
  Melihat detail catatan & menerjemahkan isi catatan.

- **helper/MappingHelper.kt**  
  Membantu konversi data dari database ke objek `Note`.

## Instalasi & Menjalankan Proyek

1. **Clone repository:**
   ```bash
   git clone https://github.com/NoisyBoy29/NotesApp.git
   ```
2. **Buka di Android Studio.**
3. **Build & Run** pada emulator atau perangkat Android.

## Dependencies

- Kotlin
- AndroidX
- Google ML Kit Translation
- RecyclerView

---

> **Catatan:**  
> Proyek ini dibuat sebagai bahan untuk Penulisan Ilmiah (PI).
