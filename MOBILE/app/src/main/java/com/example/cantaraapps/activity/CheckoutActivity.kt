    package com.example.cantaraapps.activity

    import android.app.AlertDialog
    import android.app.DatePickerDialog
    import android.content.ActivityNotFoundException
    import android.content.ClipData
    import android.content.ClipboardManager
    import android.content.Context
    import android.content.Intent
    import android.graphics.BitmapFactory
    import android.net.Uri
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Base64
    import android.util.Log
    import android.view.View
    import android.widget.Toast
    import androidx.activity.result.ActivityResultLauncher
    import androidx.activity.result.contract.ActivityResultContracts
    import com.android.volley.AuthFailureError
    import com.android.volley.RequestQueue
    import com.android.volley.Response
    import com.android.volley.toolbox.StringRequest
    import com.android.volley.toolbox.Volley
    import com.example.cantaraapps.database.DbContract
    import com.example.cantaraapps.databinding.ActivityCheckoutBinding
    import java.io.InputStream
    import java.text.SimpleDateFormat
    import java.util.Calendar
    import java.util.Date
    import java.util.Locale
    import java.util.Random
    import java.util.concurrent.TimeUnit

    class CheckoutActivity : AppCompatActivity() {
        private lateinit var binding: ActivityCheckoutBinding
        private var imageUri: Uri? = null
        private var isDateSelected = false
        private var isImageSelected = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityCheckoutBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
            val iduser = sharedPref.getString("id_user", "")
            val nama = sharedPref.getString("nama", "")
            val telp = sharedPref.getString("telp", "")
            val alamat = sharedPref.getString("alamat_lengkap", "")
            val kecamatan = sharedPref.getString("kecamatan", "")
            val namaTelp = "$nama - ($telp)"
            val fullAddress = "Kecamatan $kecamatan, $alamat"
            val textToCopy = "6282389422820"
            val ongkirr = 10000


            val idKue = intent.getStringExtra("id_kue")
            val namaKue = intent.getStringExtra("nama_kue")
            val hargaKue = intent.getStringExtra("harga_kue")
            val satuan = intent.getStringExtra("satuan")
            val gambar = intent.getStringExtra("gambar")
            val jumlah = intent.getStringExtra("jumlah")
            val totalHarga = intent.getStringExtra("total_harga")?.toInt() ?: 0
            val totalsemua = totalHarga + ongkirr
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            binding.alamatuser.text = fullAddress
            binding.namadantelp.text = namaTelp
            binding.ongkoskrm.text = "Untuk ongkos kirim anda - Rp.${ongkirr}"
            binding.totalhrg.text = "Rp. $totalsemua"
            binding.tglpemesanan.text = currentDate
            binding.namaKue.text = namaKue
            binding.hrgkue.text = "Rp. $hargaKue"
            binding.bnykkue.text = "$jumlah $satuan"
            binding.tampilbukti.visibility = View.GONE
            binding.nomerakun.text = textToCopy

            binding.floatingActionButton.setOnClickListener{
                onBackPressed()
            }

            binding.btnPilihGambar.setOnClickListener {
                pickImage.launch("image/*")
            }

            binding.copyBCA.setOnClickListener {
                copyToClipboard(textToCopy)
            }

            binding.tglditerima.setOnClickListener {
                showDatePickerDialog()
            }

            binding.buttonPesan.setOnClickListener {
                showConfirmationDialog(iduser, totalHarga, idKue, jumlah)
            }

            binding.btnKonfirmasiPembayaran.setOnClickListener {
                if (!isDateSelected) {
                    Toast.makeText(applicationContext, "Tanggal belum dipilih", Toast.LENGTH_SHORT).show()
                } else {
                    sendWhatsAppMessage(iduser)
                }
            }

            if (!gambar.isNullOrEmpty()) {
                val decodedImage = Base64.decode(gambar, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
                binding.gambarKue.setImageBitmap(bitmap)
            }
        }

        private val pickImage: ActivityResultLauncher<String> = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                binding.tampilbukti.setImageURI(uri)
                binding.tampilbukti.visibility = View.VISIBLE
                isImageSelected = true
            }
        }

        private fun showConfirmationDialog(
            iduser: String?,
            totalHarga: Int?,
            idKue: String?,
            jumlah: String?
        ) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Pesan")
            alertDialogBuilder.setMessage("Apakah anda sudah mengkonfirmasi pembayaran?")

            alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
                insertDataToDatabase(iduser, totalHarga, idKue, jumlah)
            }

            alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        private fun copyToClipboard(text: CharSequence) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", text)
            clipboardManager.setPrimaryClip(clipData)
        }

        private fun showDatePickerDialog() {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, monthOfYear, dayOfMonth)

                    val selisihMillis = selectedCalendar.timeInMillis - currentDate.timeInMillis
                    val selisihHari = TimeUnit.MILLISECONDS.toDays(selisihMillis)

                    if (selisihHari >= 3) {
                        val newDate = selectedCalendar.time
                        val tanggalDiterima = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate)
                        binding.tglditerima.text = tanggalDiterima
                        isDateSelected = true
                    } else {
                        Toast.makeText(this, "Jarak tanggal penerimaan terlalu dekat." +
                                " Silahkan pilih tanggal lain", Toast.LENGTH_SHORT).show()
                        isDateSelected = false
                    }
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        private fun generateIdPesanan(): String {
            val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            val randomCode = generateRandomCode()
            return "P$currentDate$randomCode"
        }

        private fun generateIdDetailPesanan(): String {
            val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            val randomCode = generateRandomCode()
            return "DP$currentDate$randomCode"
        }

        private fun generateRandomCode(): String {
            val random = Random()
            val randomCode = StringBuilder()
            repeat(3) {
                randomCode.append(random.nextInt(10))
            }
            return randomCode.toString()
        }

        private fun sendWhatsAppMessage(iduser: String?) {
            val nomorTelepon = "6285785932132"
            val idPesanan = generateIdPesanan()

            val pesan = "Hai admin, saya ingin konfirmasi pembayaran mengenai pemesanan kue saya\n\n" +
                    "ID Pesanan: $idPesanan\n" +
                    "ID User: $iduser\n" +
                    "Nama Kue: ${binding.namaKue.text}\n" +
                    "Harga Kue: Rp. ${binding.hrgkue.text}\n" +
                    "Jumlah: ${binding.bnykkue.text}\n" +
                    "Total Harga: Rp. ${binding.totalhrg.text}\n" +
                    "Tanggal Pemesanan: ${binding.tglpemesanan.text}\n" +
                    "Tanggal Penerimaan: ${binding.tglditerima.text}\n" +
                    "Pesan: ${binding.edtPesan.text}"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$nomorTelepon&text=$pesan")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "WhatsApp tidak terinstal.", Toast.LENGTH_SHORT).show()
            }
        }

        private fun insertDataToDatabase(
            iduser: String?,
            totalHarga: Int?,
            idKue: String?,
            jumlah: String?
        ) {
            val idPesanan = generateIdPesanan()
            val idDetailPesanan = generateIdDetailPesanan()
            val pesanText = binding.edtPesan.text.toString()
            val ongkir = 10000
            val totalsemua = totalHarga!! + ongkir

            if (!isDateSelected) {
                Toast.makeText(applicationContext, "Tanggal belum dipilih", Toast.LENGTH_SHORT).show()
                return
            }

            if (!isImageSelected) {
                Toast.makeText(applicationContext, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                return
            }

            if (pesanText.isBlank()) {
                Toast.makeText(applicationContext, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
                return
            }

            val stringRequest: StringRequest = object : StringRequest(
                Method.POST,
                DbContract.urlPesanan,
                Response.Listener { response ->
                    Log.d("InsertData", "Response: $response")
                    Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()
                    finish()
                },
                Response.ErrorListener { error ->
                    Log.e("InsertData", "Error: $error")
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["id_pesanan"] = idPesanan
                    params["id_user"] = iduser.toString()
                    params["total_harga"] = totalsemua.toString()
                    params["tgl_pesan"] = binding.tglpemesanan.text.toString()
                    params["tgl_terima"] = binding.tglditerima.text.toString()
                    params["pesan"] = pesanText
                    params["id_detailpesanan"] = idDetailPesanan
                    params["id_kue"] = idKue.toString()
                    params["jumlah_pesan"] = jumlah.toString()
                    params["harga"] = totalHarga.toString()
                    if (imageUri != null) {
                        val inputStream: InputStream? =
                            contentResolver.openInputStream(imageUri!!)
                        val bytes: ByteArray = inputStream!!.readBytes()
                        val base64Image: String = Base64.encodeToString(bytes, Base64.DEFAULT)
                        params["bukti"] = base64Image
                    } else {
                        params["bukti"] = ""
                    }
                    return params
                }
            }
            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        }
    }