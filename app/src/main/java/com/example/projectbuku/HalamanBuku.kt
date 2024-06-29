package com.example.projectbuku

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HalamanBuku : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private var tokopediaLink: String? = null
    private var shopeeLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buku)

        val backButton: ImageView = findViewById(R.id.backButton)
        val tokopediaButton: LinearLayout = findViewById(R.id.tokopediaButton)
        val shopeeButton: LinearLayout = findViewById(R.id.shopeeButton)
        val imgBuku: ImageView = findViewById(R.id.bookCover)
        val judulBuku: TextView = findViewById(R.id.txtJudul)
        val penulisBuku: TextView = findViewById(R.id.txtPenulis)
        val sinopsisBuku: TextView = findViewById(R.id.txtSinopsis)

        // Initialize Firestore
        firestore = Firebase.firestore

        // Get the book title or other identifier from the Intent
        val bookTitle = intent.getStringExtra("judul")

        // Fetch data from Firestore
        if (!bookTitle.isNullOrEmpty()) {
            fetchBookDetails(bookTitle, imgBuku, judulBuku, penulisBuku, sinopsisBuku)
        }

        backButton.setOnClickListener {
            finish()
        }

        tokopediaButton.setOnClickListener {
            tokopediaLink?.let {
                openWebsite(it)
            } ?: run {
                // Handle case where the link is not available
                Log.d("HalamanBuku", "Tokopedia link not available")
            }
        }

        shopeeButton.setOnClickListener {
            shopeeLink?.let {
                openWebsite(it)
            } ?: run {
                // Handle case where the link is not available
                Log.d("HalamanBuku", "Shopee link not available")
            }
        }
    }

    private fun fetchBookDetails(title: String, imgBuku: ImageView, judulBuku: TextView, penulisBuku: TextView, sinopsisBuku: TextView) {
        firestore.collection("daftarBuku")
            .whereEqualTo("Judul", title)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val document = result.documents[0]
                    val img = document.getString("img") ?: ""
                    val judul = document.getString("Judul") ?: "Judul tidak tersedia"
                    val penulis = document.getString("Penulis") ?: "Penulis tidak tersedia"
                    val sinopsis = document.getString("Sinopsis") ?: "Sinopsis tidak tersedia"
                    tokopediaLink = document.getString("TokoPedia")
                    shopeeLink = document.getString("Shopee")

                    // Set data to views
                    if (img.isNotEmpty()) {
                        Glide.with(this).load(img).into(imgBuku)
                    } else {
                        imgBuku.setImageResource(R.drawable.ic_launcher_foreground)
                    }

                    judulBuku.text = judul
                    penulisBuku.text = penulis
                    sinopsisBuku.text = sinopsis
                } else {
                    Log.d("HalamanBuku", "No matching documents.")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("HalamanBuku", "Error getting documents: ", exception)
            }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
d