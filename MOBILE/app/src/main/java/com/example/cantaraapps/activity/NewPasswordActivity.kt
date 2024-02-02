package com.example.cantaraapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.R
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.ActivityNewPasswordBinding

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.buttonUbahPass.setOnClickListener{
            gantiPassword()
//            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnKembaliPassbaru.setOnClickListener {
            onBackPressed()
        }
    }
    private fun gantiPassword() {
        val username = intent.getStringExtra("username")
        val passBaru = binding.edtPasswordBaru.text.toString()
        val konfirmasiPass = binding.edtKonfirPassword.text.toString()

        if (!username.isNullOrBlank() && passBaru.isNotBlank() && konfirmasiPass.isNotBlank()) {
            if (passBaru == konfirmasiPass) {

                val stringRequest: StringRequest = object : StringRequest(
                    Method.POST, DbContract.urlGanti,
                    Response.Listener { response ->
                        Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["username"] = username
                        params["password"] = passBaru
                        return params
                    }
                }

                val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
                requestQueue.add(stringRequest)

            } else {
                Toast.makeText(applicationContext, "Password tidak sesuai", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Ada data yang masih kosong", Toast.LENGTH_SHORT).show()
        }
    }
}