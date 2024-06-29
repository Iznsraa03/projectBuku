// BukuAdapter.kt
package com.example.projectbuku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class BukuAdapter(
    private val context: Context,
    private var bukuList: List<Buku>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>(), Filterable {

    private var bukuListFull: List<Buku> = ArrayList(bukuList)

    interface OnItemClickListener {
        fun onItemClick(buku: Buku)
    }

    class BukuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView = view.findViewById(R.id.imgBuku)
        val judulView: TextView = view.findViewById(R.id.judul)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_buku, parent, false)
        return BukuViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]
        holder.judulView.text = buku.judul
        Glide.with(context).load(buku.img).into(holder.imgView)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(buku)
        }
    }

    override fun getItemCount(): Int = bukuList.size

    override fun getFilter(): Filter {
        return bukuFilter
    }

    private val bukuFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Buku> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(bukuListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                for (buku in bukuListFull) {
                    if (buku.judul.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(buku)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            bukuList = results?.values as List<Buku>
            notifyDataSetChanged()
        }
    }
}
