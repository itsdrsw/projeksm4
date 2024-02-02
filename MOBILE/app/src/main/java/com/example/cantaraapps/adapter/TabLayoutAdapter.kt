package com.example.cantaraapps.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cantaraapps.ui.fragment.Dibuat
import com.example.cantaraapps.ui.fragment.Dikirim
import com.example.cantaraapps.ui.fragment.Disetujui
import com.example.cantaraapps.ui.fragment.Ditolak
import com.example.cantaraapps.ui.fragment.Permintaan
import com.example.cantaraapps.ui.fragment.Selesai

internal class TabLayoutAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                Permintaan()
            }
            1 -> {
                Disetujui()
            }
            2 -> {
                Dibuat()
            }
            3 -> {
                Dikirim()
            }
            4 -> {
                Selesai()
            }
            5 -> {
                Ditolak()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

}