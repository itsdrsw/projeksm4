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
import com.example.cantaraapps.data.RiwayatModel

class DitolakAdapter (
    private val context: Context,
    private val ditolakList: List<RiwayatModel>,
    private val listener: DitolakListener
) : RecyclerView.Adapter<DitolakAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaKueTextView: TextView = itemView.findViewById(R.id.namaKue)
        val jumlahTextView: TextView = itemView.findViewById(R.id.jumlahKue)
        val gambarImageView: ImageView = itemView.findViewById(R.id.gambarKue)
        val totalpesanTextView: TextView = itemView.findViewById(R.id.totalpesanan)
        val keteranganTextView: TextView = itemView.findViewById(R.id.keterangan)
        val tglterimaTextView: TextView = itemView.findViewById(R.id.tgl_diterima)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_riwayat_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentKue = ditolakList[position]
        holder.namaKueTextView.text = "Kue ${currentKue.namaKue}"
        holder.jumlahTextView.text = "${currentKue.jumlahPesan} ${currentKue.satuan}"
        holder.totalpesanTextView.text = "Total Pesanan: Rp.${currentKue.totalHarga}"
        holder.keteranganTextView.text = currentKue.ket

        val decodedImage = Base64.decode(currentKue.gambar, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
        holder.gambarImageView.setImageBitmap(bitmap)

        holder.tglterimaTextView.text = currentKue.tglTerima
    }

    override fun getItemCount(): Int {
        return ditolakList.size
    }

    // Fungsi onItemClick dihapus karena tidak digunakan
    interface DitolakListener
}