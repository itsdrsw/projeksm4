package com.example.cantaraapps.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cantaraapps.R
import com.example.cantaraapps.data.KueItemClickListener
import com.example.cantaraapps.data.KueModel

class KueAdapter(
    private val context: Context,
    private val kueList: List<KueModel>,
    private val clickListener: KueItemClickListener) :
    RecyclerView.Adapter<KueAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaKueTextView: TextView = itemView.findViewById(R.id.namaKue)
        val kategoriTextView: TextView = itemView.findViewById(R.id.kategoriKue)
        val gambarImageView: ImageView = itemView.findViewById(R.id.gambarKue)
        val hargaTextView: TextView = itemView.findViewById(R.id.hargaKue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentKue = kueList[position]
        holder.namaKueTextView.text = "Kue ${currentKue.namaKue}"
        holder.kategoriTextView.text = "Kue ${currentKue.kategori}"
        holder.hargaTextView.text = "Rp.${currentKue.hargaKue}"

        // Mendekode base64 dan menampilkan gambar
        val decodedImage = Base64.decode(currentKue.gambar, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
        holder.gambarImageView.setImageBitmap(bitmap)

        holder.itemView.setOnClickListener {
            clickListener.onKueItemClicked(currentKue)
        }
    }

    override fun getItemCount(): Int {
        return kueList.size
    }
}