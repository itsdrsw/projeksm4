package com.example.cantaraapps.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.database.DbContract
//import android.widget.Toast
//import com.android.volley.Request
//import com.android.volley.RequestQueue
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMasuk.setOnClickListener {
            loginUser()
//            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.daftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.txtLupapass.setOnClickListener {
            startActivity(Intent(this, LupaPasswordActivity::class.java))
        }
    }

    private fun loginUser() {
        val username = binding.edtUser.text.toString()
        val password = binding.edtPass.text.toString()

        if (!(username.isEmpty() || password.isEmpty())) {

            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)

            val stringRequest = StringRequest(
                Request.Method.GET, "${DbContract.urlLogin}&username=$username&password=$password",
                { response ->
                    if (response == "0") {
                        Toast.makeText(applicationContext, "Username atau Password Salah", Toast.LENGTH_SHORT).show()
                    } else {
                        val userData = JSONObject(response)
                        val sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("username", userData.getString("username"))
                        editor.putString("nama", userData.getString("nama"))
                        editor.putString("kecamatan", userData.getString("kecamatan"))
                        editor.putString("alamat_lengkap", userData.getString("alamat_lengkap"))
                        editor.putString("telp", userData.getString("telp"))
                        editor.putString("id_user", userData.getString("id_user"))
                        editor.putBoolean("is_logged_in", true)
                        editor.apply()

                        Toast.makeText(applicationContext, "Selamat Datang ${userData.getString("nama")}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))

                        finish()
                    }
                }
            ) { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(applicationContext, "Ada data yang belum diisi", Toast.LENGTH_SHORT).show()
        }
    }
}
