package com.example.projectbuku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class KategoriBuku : AppCompatActivity(), KatergoriAdapter.OnItemClickListener {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var genreTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kategori_buku)

        firestore = Firebase.firestore
        genreTextView = findViewById(R.id.Genre)
        recyclerView = findViewById(R.id.book_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnBack = findViewById(R.id.backButton)

        val genre = intent.getStringExtra("Genre")
        genre?.let {
            fetchBooksByGenre(it)
        }
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchBooksByGenre(genre: String) {
        genreTextView.text = genre

        firestore.collection("daftarBuku")
            .whereEqualTo("Genre", genre)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val books = result.map { document ->
                        val img = document.getString("img") ?: ""
                        val judul = document.getString("Judul") ?: ""
                        val penulis = document.getString("Penulis") ?: ""
                        val sinopsis = document.getString("Sinopsis") ?: ""
                        Buku(img, judul, penulis, sinopsis)
                    }
                    recyclerView.adapter = KatergoriAdapter(this, books, this)
                } else {
                    genreTextView.text = "No books found for this genre"
                }
            }
            .addOnFailureListener { exception ->
                genreTextView.text = "Error fetching books"
            }
    }

    override fun onItemClick(book: Buku) {
        // Tangani klik item di sini, misalnya dengan membuka detail buku
        // Contoh: Toast.makeText(this, book.judul, Toast.LENGTH_SHORT).show()
        val intent = Intent(this,HalamanBuku::class.java)
        startActivity(intent)
    }
}
