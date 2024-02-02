package com.example.cantaraapps.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.R
import com.example.cantaraapps.activity.KonfirmasiPassActivity
import com.example.cantaraapps.activity.LoginActivity
import com.example.cantaraapps.activity.RiwayatTransaksiActivity
import com.example.cantaraapps.activity.SettingAkunActivity
import com.example.cantaraapps.activity.UbahPassActivity
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.FragmentProfileBinding
import org.json.JSONException
import org.json.JSONObject

class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        sharedPreferences = requireActivity().getSharedPreferences("user_data", AppCompatActivity.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            val username = sharedPreferences.getString("nama", "")
            val firstChar = username?.take(1)

            binding.profileImage.text = firstChar
        }

        val sharedPref = activity?.getSharedPreferences("user_data", MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")
        val nama = sharedPref?.getString("nama", "")

        if (!username.isNullOrEmpty()) {
            binding.username.text = username
            binding.namaLengkap.text = nama
        }

        binding.btnLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_logged_in", false)
            editor.apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnSetAkun.setOnClickListener {
            val intent = Intent(requireActivity(), SettingAkunActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        binding.btnRiwayatTran.setOnClickListener {
            val intent = Intent(requireActivity(), RiwayatTransaksiActivity::class.java)
            startActivity(intent)
        }

        binding.btnUbahPass.setOnClickListener {
            val intent = Intent(requireActivity(), KonfirmasiPassActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        return binding.root
    }
}