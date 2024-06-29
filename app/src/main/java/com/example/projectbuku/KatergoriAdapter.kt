// KatergoriAdapter.kt
package com.example.projectbuku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class KatergoriAdapter(
    private val context: Context,
    private val books: List<Buku>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<KatergoriAdapter.BookViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(book: Buku)
    }

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.book_cover)
        val judulView: TextView = view.findViewById(R.id.book_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_kategori, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.judulView.text = book.judul
        Glide.with(context).load(book.img).into(holder.imgView)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(book)
        }
    }

    override fun getItemCount(): Int = books.size
}
