    package com.example.cantaraapps.activity

    import android.content.ActivityNotFoundException
    import android.content.Intent
    import android.net.Uri
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Toast
    import androidx.viewpager.widget.ViewPager
    import com.example.cantaraapps.R
    import com.example.cantaraapps.adapter.TabLayoutAdapter
    import com.example.cantaraapps.databinding.ActivityRiwayatTransaksiBinding
    import com.google.android.material.tabs.TabLayout

    class RiwayatTransaksiActivity : AppCompatActivity() {
        private lateinit var binding: ActivityRiwayatTransaksiBinding
        private lateinit var tabLayout: TabLayout
        private lateinit var viewPager: ViewPager
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityRiwayatTransaksiBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnBack.setOnClickListener {
                onBackPressed()
            }

            binding.floatingActionButton.setOnClickListener {
                val nomorTelepon = "6285785932132"
                val pesan = "Hai admin, saya ingin bertanya tentang pesanan saya!"

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$nomorTelepon&text=$pesan")

                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "WhatsApp tidak terinstal.", Toast.LENGTH_SHORT).show()
                }
            }

            tabLayout = findViewById(R.id.tabLayout)
            viewPager = findViewById(R.id.viewPager)

            tabLayout.addTab(tabLayout.newTab().setText("Permintaan"))
            tabLayout.addTab(tabLayout.newTab().setText("Disetujui"))
            tabLayout.addTab(tabLayout.newTab().setText("Dibuat"))
            tabLayout.addTab(tabLayout.newTab().setText("Dikirim"))
            tabLayout.addTab(tabLayout.newTab().setText("Selesai"))
            tabLayout.addTab(tabLayout.newTab().setText("Ditolak"))
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL

            val adapter = TabLayoutAdapter(this, supportFragmentManager,
                tabLayout.tabCount)
            viewPager.adapter = adapter

            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
    }