<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class DetailProfileSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //insert data to table pegawai
        DB::table('detail_profile')->insert([
            'address' => 'Probolinggo',
            'nomor_tlp' => '082389422820',
            'ttl' => '2003-08-25',
            'foto' => 'picture.png'
        ]);
    }
}
