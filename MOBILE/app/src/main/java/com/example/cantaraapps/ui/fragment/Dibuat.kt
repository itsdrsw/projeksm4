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
import com.example.cantaraapps.adapter.DibuatAdapter
import com.example.cantaraapps.data.RiwayatModel
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.FragmentDibuatBinding
import org.json.JSONException

class Dibuat : Fragment(), DibuatAdapter.DibuatListener {
    private lateinit var binding: FragmentDibuatBinding
    private val listDibuat = ArrayList<RiwayatModel>()
    private lateinit var adapterDibuat: DibuatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDibuatBinding.inflate(inflater, container, false)

        binding.dibuat.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapterDibuat = DibuatAdapter(requireContext(), listDibuat, this)
        binding.dibuat.adapter = adapterDibuat
        fetchDataDibuatFromServer()

        return binding.root
    }

    private fun fetchDataDibuatFromServer() {

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val idUser = sharedPreferences.getString("id_user", "") ?: ""

        val ket = "Dibuat"
        val url = "${DbContract.urlRiwayatTrans}&id_user=$idUser&ket=$ket"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listDibuat.clear()

                    for (i in 0 until response.length()) {
                        val pesananObject = response.getJSONObject(i)
                        val namaKue = pesananObject.getString("nama_kue")
                        val totalHarga = pesananObject.getString("total_harga")
                        val jumlahPesan = pesananObject.getString("jumlah_pesan")
                        val satuan = pesananObject.getString("satuan")
                        val gambar = pesananObject.getString("gambar")
                        val tglTerima = pesananObject.getString("tgl_terima")

                        val pesanan = RiwayatModel(namaKue, jumlahPesan, satuan, totalHarga, ket, gambar, tglTerima)
                        listDibuat.add(pesanan)
                    }

                    adapterDibuat.notifyDataSetChanged()

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