package com.example.cantaraapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cantaraapps.R
import com.example.cantaraapps.database.DbContract
//import android.widget.Toast
//import com.android.volley.AuthFailureError
//import com.android.volley.RequestQueue
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.example.cantaraapps.database.DbContract
import com.example.cantaraapps.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kecamatan = resources.getStringArray(R.array.lokasi_array)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, kecamatan)
        binding.autocomplatext.setAdapter(arrayAdapter)

        binding.buttonDaftar.setOnClickListener {
            registerUser()
        }

        binding.btnKembaliRegister.setOnClickListener {
            onBackPressed()
        }

    }

    private fun registerUser() {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()
        val alamatlengkap = binding.edtAlamat.text.toString()
        val nama = binding.edtNamaLengkap.text.toString()
        val telp = binding.edtTelp.text.toString()
        val security = binding.edtLupaPass.text.toString()
        val kecamatan = binding.autocomplatext.text.toString()

        if (nama.any { it.isDigit() }) {
            Toast.makeText(applicationContext, "Nama tidak boleh mengandung angka", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 4) {
            Toast.makeText(applicationContext, "Password minimal 4 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length > 8) {
            Toast.makeText(applicationContext, "Password maksimal 8 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if (telp.length < 9) {
            Toast.makeText(applicationContext, "Nomer Telepon minimal 9 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if (telp.length > 13) {
            Toast.makeText(applicationContext, "Nomer Telepon maksimal 13 karakter", Toast.LENGTH_SHORT).show()
            return
        }


        if (!(username.isEmpty() || password.isEmpty() || alamatlengkap.isEmpty() || nama.isEmpty() || telp.isEmpty() || security.isEmpty())) {

            val stringRequest: StringRequest = object : StringRequest(
                Method.POST,
                DbContract.urlRegister,
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
                    params["password"] = password
                    params["alamat_lengkap"] = alamatlengkap
                    params["nama"] = nama
                    params["telp"] = telp
                    params["security"] = security
                    params["kecamatan"] = kecamatan
                    return params
                }
            }

            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(applicationContext, "Ada data yang belum diisi", Toast.LENGTH_SHORT).show()
        }
    }
}