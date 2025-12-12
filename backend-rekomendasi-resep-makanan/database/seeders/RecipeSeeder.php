<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class RecipeSeeder extends Seeder
{
    public function run()
    {
        DB::table('recipes')->truncate();

        DB::table('recipes')->insert([
            [
                'nama_resep' => 'Rendang Sapi',
                'category_id' => 1, // Nusantara
                'deskripsi' => 'Masakan daging sapi bercita rasa pedas yang menggunakan campuran dari berbagai bumbu dan rempah-rempah.',
                'bahan' => json_encode(['Daging Sapi 1kg', 'Santan 2 Liter', 'Cabai Merah 250gr', 'Bawang Merah 100gr', 'Bawang Putih 50gr']),
                'langkah_langkah' => json_encode(['1. Potong daging sesuai selera.', '2. Haluskan bumbu.', '3. Masak santan bersama bumbu hingga mendidih.', '4. Masukkan daging, masak hingga empuk dan kuah mengering.']),
                'gambar' => '',
                'created_at' => now(),
                'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Nasi Goreng Spesial',
                'category_id' => 1, // Nusantara
                'deskripsi' => 'Nasi goreng dengan bumbu spesial, telur mata sapi, dan suwiran ayam.',
                'bahan' => json_encode(['Nasi Putih 1 Piring', 'Telur 1 Butir', 'Bawang Merah 3 Siung', 'Kecap Manis 2 sdm', 'Garam secukupnya']),
                'langkah_langkah' => json_encode(['1. Tumis bumbu halus.', '2. Masukkan telur, orak-arik.', '3. Masukkan nasi dan bumbu lain.', '4. Aduk rata, sajikan.']),
                'gambar' => '',
                'created_at' => now(),
                'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Fuyunghai',
                'category_id' => 2, // Chinese
                'deskripsi' => 'Telur dadar tebal campuran sayur dan daging dengan saus asam manis.',
                'bahan' => json_encode(['Telur 3 Butir', 'Wortel cincang', 'Kol iris tipis', 'Daging ayam cincang']),
                'langkah_langkah' => json_encode(['1. Kocok telur dengan sayur dan daging.', '2. Goreng hingga matang dan tebal.', '3. Buat saus asam manis.', '4. Siram saus di atas fuyunghai.']),
                'gambar' => '',
                'created_at' => now(),
                'updated_at' => now(),
            ],
        ]);
    }
}
