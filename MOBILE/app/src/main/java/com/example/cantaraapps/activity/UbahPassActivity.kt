package com.example.cantaraapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.ActivityUbahPassBinding

class UbahPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUbahPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSimpan.setOnClickListener {
            ubahPassword()
        }

        binding.btnKembaliLupapass.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkPasswordLength(password: String): Boolean {
        if (password.length < 4) {
            Toast.makeText(
                applicationContext,
                "Password minimal 4 karakter",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (password.length > 8) {
            Toast.makeText(
                applicationContext,
                "Password maksimal 8 karakter",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun ubahPassword() {
        val username = intent.getStringExtra("username")
        val passBaru = binding.edtPassBaru.text.toString()
        val konfirmasiPass = binding.edtKonfirPass.text.toString()

        if (checkPasswordLength(passBaru) && checkPasswordLength(konfirmasiPass)) {
            if (!username.isNullOrBlank() && passBaru.isNotBlank() && konfirmasiPass.isNotBlank()) {
                if (passBaru == konfirmasiPass) {

                    val stringRequest: StringRequest = object : StringRequest(
                        Method.POST, DbContract.urlGanti,
                        Response.Listener { response ->
                            Toast.makeText(
                                applicationContext,
                                response.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                                .show()
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
                    Toast.makeText(applicationContext, "Password tidak sesuai", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Ada data yang masih kosong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}