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
import com.example.cantaraapps.adapter.PermintaanAdapter
import com.example.cantaraapps.adapter.SelesaiAdapter
import com.example.cantaraapps.data.RiwayatModel
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.FragmentPengajuanBinding
import com.example.cantaraapps.databinding.FragmentSelesaiBinding
import org.json.JSONException

class Selesai : Fragment(), SelesaiAdapter.SelesaiListener {
    private lateinit var binding: FragmentSelesaiBinding
    private val listSelesai = ArrayList<RiwayatModel>()
    private lateinit var adapterSelesai: SelesaiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelesaiBinding.inflate(inflater, container, false)

        binding.selesai.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapterSelesai = SelesaiAdapter(requireContext(), listSelesai, this)
        binding.selesai.adapter = adapterSelesai
        fetchDataSelesaiFromServer("")

        return binding.root
    }

    private fun fetchDataSelesaiFromServer(idUser: String) {

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val idUser = sharedPreferences.getString("id_user", "") ?: ""

        val ket = "Selesai"
        val url = "${DbContract.urlRiwayatTrans}&id_user=$idUser&ket=$ket"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listSelesai.clear()

                    for (i in 0 until response.length()) {
                        val pesananObject = response.getJSONObject(i)
                        val namaKue = pesananObject.getString("nama_kue")
                        val totalHarga = pesananObject.getString("total_harga")
                        val jumlahPesan = pesananObject.getString("jumlah_pesan")
                        val satuan = pesananObject.getString("satuan")
                        val gambar = pesananObject.getString("gambar")
                        val tglTerima = pesananObject.getString("tgl_terima")

                        val pesanan = RiwayatModel(namaKue, jumlahPesan, satuan, totalHarga, ket, gambar, tglTerima)
                        listSelesai.add(pesanan)
                    }

                    adapterSelesai.notifyDataSetChanged()

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