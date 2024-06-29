package com.example.projectbuku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), BukuAdapter.OnItemClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var cardAdapter: CardAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var bestsellerBooksAdapter: BukuAdapter
    private lateinit var romanceButton: AppCompatButton
    private lateinit var horrorButton: AppCompatButton
    private lateinit var fantasyButton: AppCompatButton
    private lateinit var mysteryButton: AppCompatButton
    private lateinit var sciFiButton: AppCompatButton
    private lateinit var komediBtn: AppCompatButton
    private lateinit var thrillerBtn: AppCompatButton
    private lateinit var searchView: SearchView

    private lateinit var firestore: FirebaseFirestore

    companion object {
        private const val COLLECTION_NAME = "daftarBuku"
        private const val GENRE_EXTRA = "Genre"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestore = Firebase.firestore

        initializeUIComponents()
        setupGenreButtons()
        setupViewPager()
        fetchBestsellerBooks()
        setupSearchView()
    }

    private fun initializeUIComponents() {
        viewPager = findViewById(R.id.image_carousel)
        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.bestseller_books)
        romanceButton = findViewById(R.id.btnRomance)
        fantasyButton = findViewById(R.id.btnFantasi)
        horrorButton = findViewById(R.id.btnHorror)
        mysteryButton = findViewById(R.id.btnMisteri)
        sciFiButton = findViewById(R.id.btnScifi)
        komediBtn = findViewById(R.id.btnKomedi)
        thrillerBtn = findViewById(R.id.btnThriller)
        searchView = findViewById(R.id.search_view)
    }

    private fun setupGenreButtons() {
        romanceButton.setOnClickListener { openGenreActivity("Romance") }
        fantasyButton.setOnClickListener { openGenreActivity("Fiksi") }
        horrorButton.setOnClickListener { openGenreActivity("Horor") }
        mysteryButton.setOnClickListener { openGenreActivity("Misteri") }
        komediBtn.setOnClickListener { openGenreActivity("Komedi") }
        thrillerBtn.setOnClickListener { openGenreActivity("Thriller") }
        sciFiButton.setOnClickListener { openGenreActivity("Sci-Fi") }
    }

    private fun openGenreActivity(genre: String) {
        val intent = Intent(this, KategoriBuku::class.java).apply {
            putExtra(GENRE_EXTRA, genre)
        }
        startActivity(intent)
    }

    private fun setupViewPager() {
        val cardItems = listOf(
            Card(R.drawable.rectangle_2, "Card 1"),
            Card(R.drawable.rectangle_2, "Card 2"),
            Card(R.drawable.rectangle_2, "Card 3")
        )
        cardAdapter = CardAdapter(this, cardItems)
        viewPager.adapter = cardAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            tab.text = " "
        }.attach()
    }

    private fun fetchBestsellerBooks() {
        firestore.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                val bestsellerBooks = result.map { document ->
                    Buku(
                        document.getString("img") ?: "",
                        document.getString("Judul") ?: "",
                        document.getString("Penulis") ?: "",
                        document.getString("Sinopsis") ?: ""
                    )
                }
                setupRecyclerView(bestsellerBooks)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupRecyclerView(bestsellerBooks: List<Buku>) {
        bestsellerBooksAdapter = BukuAdapter(this, bestsellerBooks, this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = bestsellerBooksAdapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                bestsellerBooksAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onItemClick(buku: Buku) {
        val intent = Intent(this, HalamanBuku::class.java).apply {
            putExtra("judul", buku.judul)
        }
        startActivity(intent)
    }
}
