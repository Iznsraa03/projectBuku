package com.example.projectbuku

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class KategoriBuku : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var genreTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kategori_buku)

        firestore = Firebase.firestore
        genreTextView = findViewById(R.id.Genre)
        recyclerView = findViewById(R.id.book_recyclerview)
        searchView = findViewById(R.id.search_view)

        val genre = intent.getStringExtra("Genre")
        genre?.let {
            fetchBooksByGenre(it)
        }
    }

    private fun fetchBooksByGenre(genre: String) {
        genreTextView.text = genre

        firestore.collection("daftarBuku")
            .whereEqualTo("Genre", genre)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // Here you would normally set up your RecyclerView adapter with the books data
                    // Assuming you have a BukuAdapter, you can pass the data to it
                    val books = result.map { document ->
                        val img = document.getString("img") ?: ""
                        val judul = document.getString("Judul") ?: ""
                        val penulis = document.getString("Penulis") ?: ""
                        val sinopsis = document.getString("Sinopsis") ?: ""
                        Buku(img, judul, penulis, sinopsis)
                    }
                    recyclerView.adapter = BukuAdapter(this, books)
                } else {
                    genreTextView.text = "No books found for this genre"
                }
            }
            .addOnFailureListener { exception ->
                genreTextView.text = "Error fetching books"
            }
    }
}
