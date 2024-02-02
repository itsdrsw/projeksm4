package com.example.cantaraapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.ActivityLupaPasswordBinding

class LupaPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLupaPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonKonfirmasi.setOnClickListener {
            konfirmasiUser()
//            startActivity(Intent(this, NewPasswordActivity::class.java))
        }

        binding.btnKonfirAkun.setOnClickListener {
            onBackPressed()
        }
    }

    private fun konfirmasiUser() {
        val username = binding.edtKonfirUsername.text.toString()
        val security = binding.edtMakananFav.text.toString()

        if (!(username.isEmpty() || security.isEmpty())) {

            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)


            val stringRequest = StringRequest(
                Request.Method.GET, "${DbContract.urlLupa}&username=$username&security=$security",
                { response ->
                    if (response == "Tolong Masukkan Password Baru") {
                        Toast.makeText(applicationContext, "Konfirmasi Berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, NewPasswordActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Username atau Makanan Favorit salah", Toast.LENGTH_SHORT).show()
                    }
                }
            ) { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(applicationContext, "Tolong lengkapi data", Toast.LENGTH_SHORT).show()
        }
    }
}