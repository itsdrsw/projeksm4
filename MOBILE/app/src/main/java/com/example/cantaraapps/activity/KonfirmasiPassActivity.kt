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
import com.example.cantaraapps.databinding.ActivityKonfirmasiPassBinding

class KonfirmasiPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKonfirmasiPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonfirmasiPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonKonfirmasipass.setOnClickListener {
            konfirmasiPass()
        }

        binding.btnKembaliUbapass.setOnClickListener {
            onBackPressed()
        }
    }
    private fun konfirmasiPass() {
        val sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")
        val password = binding.edtPassLama.text.toString()

        if (!(username!!.isEmpty() || password.isEmpty())) {

            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)

            val stringRequest = StringRequest(
                Request.Method.GET, "${DbContract.urlKonfirPass}&username=$username&password=$password",
                { response ->
                    if (response == "Tolong Masukkan Password Baru") {
                        Toast.makeText(applicationContext, "Konfirmasi Berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, UbahPassActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Password Tidak Sesuai", Toast.LENGTH_SHORT).show()
                    }
                }
            ) { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(applicationContext, "Tolong Lengkapi Data", Toast.LENGTH_SHORT).show()
        }
    }
}