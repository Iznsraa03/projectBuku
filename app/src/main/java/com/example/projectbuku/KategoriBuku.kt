// KategoriBuku.kt
package com.example.projectbuku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class KategoriBuku : AppCompatActivity(), KatergoriAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KatergoriAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kategori_buku)

        recyclerView = findViewById(R.id.book_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        firestore = Firebase.firestore

        val genre = intent.getStringExtra("Genre") ?: ""
        fetchBooksByGenre(genre)
    }

    private fun fetchBooksByGenre(genre: String) {
        firestore.collection("daftarBuku")
            .whereEqualTo("Genre", genre)
            .get()
            .addOnSuccessListener { result ->
                val books = result.map { document ->
                    val img = document.getString("img") ?: ""
                    val judul = document.getString("Judul") ?: ""
                    val penulis = document.getString("Penulis") ?: "" // Tambahkan ini
                    val sinopsis = document.getString("Sinopsis") ?: ""
                    Buku(img, judul, penulis, sinopsis)
                }
                adapter = KatergoriAdapter(this, books, this)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onItemClick(book: Buku) {
        val intent = Intent(this, HalamanBuku::class.java)
        intent.putExtra("img", book.img)
        intent.putExtra("judul", book.judul)
        intent.putExtra("penulis", book.penulis)
        intent.putExtra("sinopsis", book.sinopsis)
        startActivity(intent)
    }
}
