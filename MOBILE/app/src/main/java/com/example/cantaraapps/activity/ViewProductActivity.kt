package com.example.cantaraapps.activity

import android.R.id
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.widget.Toast
import com.example.cantaraapps.databinding.ActivityViewProductBinding
import java.text.NumberFormat
import java.util.Locale

class ViewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewProductBinding
    private var idKue: String? = null
    private var namaKue: String? = null
    private var hargaKue: String? = null
    private var kategori: String? = null
    private var jumlah: String? = null
    private var satuan: String? = null
    private var gambar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            onBackPressed()
        }

        binding.buttonPesan.setOnClickListener {
            // Dapatkan jumlah dari EditText
            val quantity = binding.editText.text.toString().toInt()

            // Hitung total harga
            val totalHarga = quantity * hargaKue!!.toInt()

            // Kirim data ke CheckoutActivity
            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("id_kue", idKue)
                putExtra("nama_kue", namaKue)
                putExtra("harga_kue", hargaKue)
                putExtra("gambar", gambar)
                putExtra("kategori", kategori)
                putExtra("satuan", satuan)
                putExtra("jumlah", quantity.toString())
                putExtra("total_harga", totalHarga.toString())
            }
            startActivity(intent)
        }

        val editText = binding.editText
        val btnPlus = binding.btnPlus
        val btnMinus = binding.btnMinus

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.toString() == "0") {
                    editText.setText("")
                }
            }
        })

        btnPlus.setOnClickListener {
            val valueStr = editText.text.toString()

            var value = Integer.parseInt(valueStr)

            value++

            editText.setText(value.toString())
        }

        btnMinus.setOnClickListener {
            val valueStr = editText.text.toString()

            var value = Integer.parseInt(valueStr)

            if (value > 1) {
                value--
                editText.setText(value.toString())
            } else {
                btnMinus.isEnabled = false
            }
        }

        idKue = intent.getStringExtra("id_kue")
        namaKue = intent.getStringExtra("nama_kue")
        hargaKue = intent.getStringExtra("harga")
        gambar = intent.getStringExtra("gambar")
        kategori = intent.getStringExtra("kategori")
        jumlah = intent.getStringExtra("jumlah")
        satuan = intent.getStringExtra("satuan")

        binding.namaKue.text = "Kue $namaKue"
        binding.hargaKue.text = "Rp.$hargaKue"
        binding.kategori.text = "Kue $kategori"
        binding.satuan.text = satuan
        binding.jumlah.text = jumlah

        if (gambar != null && gambar!!.isNotEmpty()) {
            val decodedImage = Base64.decode(gambar, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
            binding.gambarKue.setImageBitmap(bitmap)
        }
    }
}