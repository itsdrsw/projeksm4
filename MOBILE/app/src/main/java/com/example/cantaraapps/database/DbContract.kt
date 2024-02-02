package com.example.cantaraapps.database

object DbContract {
    private const val ip = "https://cantara.tifc.myhost.id/cantara/testapi"
    const val urlLogin = "$ip/api.php?function=login"
    const val urlRegister = "$ip/api.php?function=register"
    const val urlLupa = "$ip/api.php?function=lupapassword"
    const val urlGanti = "$ip/api.php?function=gantipassword"
    const val urlHomeKue = "$ip/api.php?function=home"
    const val urlUpdateAkun = "$ip/api.php?function=ubahakun"
    const val urlKonfirPass = "$ip/api.php?function=konfirmasipassword"
    const val urlPesanan = "$ip/api.php?function=pesanan"
    const val urlCariKue = "$ip/api.php?function=carikue"
    const val urlRiwayatTrans = "$ip/api.php?function=riwayattransaksi"
}