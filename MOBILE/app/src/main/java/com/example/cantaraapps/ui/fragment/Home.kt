package com.example.cantaraapps.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cantaraapps.R
import com.example.cantaraapps.adapter.ImageAdapterSlider
import com.example.cantaraapps.data.ImageDataBanner
import com.example.cantaraapps.data.KueModel
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.FragmentHomeBinding
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.activity.ViewProductActivity
import com.example.cantaraapps.adapter.KueAdapter
import com.example.cantaraapps.data.KueItemClickListener
import org.json.JSONException
import java.util.Locale

class Home : Fragment(), KueItemClickListener {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapterSlider: ImageAdapterSlider
    private val listBanner = ArrayList<ImageDataBanner>()
    private lateinit var dots: ArrayList<TextView>
    private val bannerHandler = Handler()
    private val bannerRunnable = Runnable {
        val currentItem = binding.viewPager.currentItem
        val nextItem = if (currentItem == listBanner.size - 1) 0 else currentItem + 1
        binding.viewPager.setCurrentItem(nextItem, true)
    }
    private val bannerDelay = 5000L

    private val listKue = ArrayList<KueModel>()
    private lateinit var adapterKue: KueAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setCategoryButtonState(binding.ktgr1.id)
        binding.ktgr1.setOnClickListener {
            setCategoryButtonState(binding.ktgr1.id)
            fetchDataKueFromServer("semua")
        }

        binding.ktgr2.setOnClickListener {
            setCategoryButtonState(binding.ktgr2.id)
            fetchDataKueFromServer("hajatan")
        }

        binding.ktgr3.setOnClickListener {
            setCategoryButtonState(binding.ktgr3.id)
            fetchDataKueFromServer("kering")
        }

        binding.whatsapp.setOnClickListener {
            val nomorTelepon = "6285785932132"
            val pesan = "Hai admin, saya ingin bertanya tentang pembelian kue"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$nomorTelepon&text=$pesan")

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "WhatsApp tidak terinstal.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isAdded) {
                    selectedDot(position)
                    bannerHandler.removeCallbacks(bannerRunnable)
                    bannerHandler.postDelayed(bannerRunnable, bannerDelay)
                }
                super.onPageSelected(position)
            }
        })

        listBanner.add(ImageDataBanner(R.drawable.banner))
        listBanner.add(ImageDataBanner(R.drawable.banner1))
        listBanner.add(ImageDataBanner(R.drawable.banner2))
        adapterSlider = ImageAdapterSlider(requireContext(), listBanner)
        binding.viewPager.adapter = adapterSlider
        dots = ArrayList()
        setIndicator()


        binding.tampilanKue.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        adapterKue = KueAdapter(requireContext(), listKue, this)
        binding.tampilanKue.adapter = adapterKue
        fetchDataKueFromServer("semua")

        binding.viewPager.postDelayed(bannerRunnable, bannerDelay)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchKueFromServer(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopBannerAutoScroll()
    }

    private fun stopBannerAutoScroll() {
        bannerHandler.removeCallbacks(bannerRunnable)
    }

    private fun selectedDot(position: Int) {
        requireActivity().runOnUiThread {
            for (i in 0 until listBanner.size) {
                if (i == position)
                    dots[i].setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            com.google.android.material.R.color.m3_ref_palette_white
                        )
                    )
                else
                    dots[i].setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            com.google.android.material.R.color.design_default_color_secondary
                        )
                    )
            }
        }
    }

    private fun setIndicator() {
        for (i in 0 until listBanner.size) {
            dots.add(TextView(requireContext()))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                dots[i].text = Html.fromHtml("&#9679")
            }
            dots[i].textSize = 10f
            binding.dots.addView(dots[i])
        }
    }

    private fun fetchDataKueFromServer(kategori: String) {
        val url = if (kategori.toLowerCase(Locale.ROOT) == "semua") {
            DbContract.urlHomeKue
        } else {
            "${DbContract.urlHomeKue}&kategori=$kategori"
        }

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listKue.clear()

                    for (i in 0 until response.length()) {
                        val kueObject = response.getJSONObject(i)
                        val idKue = kueObject.getString("id_kue")
                        val namaKue = kueObject.getString("nama_kue")
                        val kategori = kueObject.getString("kategori")
                        val gambar = kueObject.getString("gambar")
                        val hargaKue = kueObject.getString("harga")
                        val jumlah = kueObject.getString("jumlah")
                        val satuan = kueObject.getString("satuan")

                        val kue = KueModel(idKue, namaKue, kategori, gambar, hargaKue, jumlah, satuan)
                        listKue.add(kue)
                    }

                    adapterKue.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest)
    }

    private fun searchKueFromServer(namaKue: String) {
        val url = "${DbContract.urlCariKue}&nama_kue=$namaKue"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listKue.clear()

                    for (i in 0 until response.length()) {
                        val kueObject = response.getJSONObject(i)
                        val idKue = kueObject.getString("id_kue")
                        val namaKue = kueObject.getString("nama_kue")
                        val kategori = kueObject.getString("kategori")
                        val gambar = kueObject.getString("gambar")
                        val hargaKue = kueObject.getString("harga")
                        val jumlah = kueObject.getString("jumlah")
                        val satuan = kueObject.getString("satuan")

                        val kue = KueModel(idKue, namaKue, kategori, gambar, hargaKue, jumlah, satuan)
                        listKue.add(kue)
                    }

                    adapterKue.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest)
    }

    override fun onKueItemClicked(kue: KueModel) {
        val intent = Intent(requireContext(), ViewProductActivity::class.java)
        intent.putExtra("id_kue", kue.idKue)
        intent.putExtra("nama_kue", kue.namaKue)
        intent.putExtra("kategori", kue.kategori)
        intent.putExtra("gambar", kue.gambar)
        intent.putExtra("harga", kue.hargaKue)
        intent.putExtra("jumlah", kue.jumlah)
        intent.putExtra("satuan", kue.satuan)
        startActivity(intent)
    }

    private fun setCategoryButtonState(selectedButtonId: Int) {
        val buttons = arrayOf(binding.ktgr1, binding.ktgr2, binding.ktgr3)

        for (button in buttons) {
            if (button.id == selectedButtonId) {
                button.setBackgroundResource(R.drawable.button_homekategori_gradient)
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                button.setBackgroundResource(R.drawable.rounded_border_gray)
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_700))
            }
        }
    }
}
