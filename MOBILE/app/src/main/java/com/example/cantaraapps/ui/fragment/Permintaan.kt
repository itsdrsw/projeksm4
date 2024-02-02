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
import com.example.cantaraapps.adapter.PermintaanAdapter
import com.example.cantaraapps.data.RiwayatModel
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.FragmentPengajuanBinding
import org.json.JSONException

class Permintaan : Fragment(), PermintaanAdapter.PermintaanListener {
    private lateinit var binding: FragmentPengajuanBinding
    private val listPermintaan = ArrayList<RiwayatModel>()
    private lateinit var adapterPermintaan: PermintaanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPengajuanBinding.inflate(inflater, container, false)

        binding.permintaan.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapterPermintaan = PermintaanAdapter(requireContext(), listPermintaan, this)
        binding.permintaan.adapter = adapterPermintaan
        fetchDataPesananFromServer("")

        return binding.root
    }

    private fun fetchDataPesananFromServer(idUser: String) {

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val idUser = sharedPreferences.getString("id_user", "") ?: ""

        val ket = "Diminta"
        val url = "${DbContract.urlRiwayatTrans}&id_user=$idUser&ket=$ket"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listPermintaan.clear()

                    for (i in 0 until response.length()) {
                        val pesananObject = response.getJSONObject(i)
                        val namaKue = pesananObject.getString("nama_kue")
                        val totalHarga = pesananObject.getString("total_harga")
                        val jumlahPesan = pesananObject.getString("jumlah_pesan")
                        val satuan = pesananObject.getString("satuan")
                        val gambar = pesananObject.getString("gambar")
                        val tglTerima = pesananObject.getString("tgl_terima")

                        val pesanan = RiwayatModel(namaKue, jumlahPesan, satuan, totalHarga, ket, gambar, tglTerima)
                        listPermintaan.add(pesanan)
                    }

                    adapterPermintaan.notifyDataSetChanged()

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