package com.example.cantaraapps.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.R
import com.example.cantaraapps.adapter.DisetujuiAdapter
import com.example.cantaraapps.adapter.DitolakAdapter
import com.example.cantaraapps.data.RiwayatModel
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.FragmentDisetujuiBinding
import com.example.cantaraapps.databinding.FragmentDitolakBinding
import org.json.JSONException

class Ditolak : Fragment(), DitolakAdapter.DitolakListener {
    private lateinit var binding: FragmentDitolakBinding
    private val listDitolak = ArrayList<RiwayatModel>()
    private lateinit var adapterDitolak: DitolakAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDitolakBinding.inflate(inflater, container, false)

        binding.ditolak.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapterDitolak = DitolakAdapter(requireContext(), listDitolak, this)
        binding.ditolak.adapter = adapterDitolak
        fetchDataDitolakFromServer("")

        return binding.root
    }

    private fun fetchDataDitolakFromServer(idUser: String) {

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val idUser = sharedPreferences.getString("id_user", "") ?: ""

        val ket = "Ditolak"
        val url = "${DbContract.urlRiwayatTrans}&id_user=$idUser&ket=$ket"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listDitolak.clear()

                    for (i in 0 until response.length()) {
                        val pesananObject = response.getJSONObject(i)
                        val namaKue = pesananObject.getString("nama_kue")
                        val totalHarga = pesananObject.getString("total_harga")
                        val jumlahPesan = pesananObject.getString("jumlah_pesan")
                        val satuan = pesananObject.getString("satuan")
                        val gambar = pesananObject.getString("gambar")
                        val tglTerima = pesananObject.getString("tgl_terima")

                        val pesanan = RiwayatModel(namaKue, jumlahPesan, satuan, totalHarga, ket, gambar, tglTerima)
                        listDitolak.add(pesanan)
                    }

                    adapterDitolak.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest)
    }
}