<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

class RecipeSeeder extends Seeder
{
    public function run()
    {
        // Disable Foreign Key Check untuk Truncate aman
        Schema::disableForeignKeyConstraints();
        DB::table('recipes')->truncate();
        Schema::enableForeignKeyConstraints();

        $recipes = [
            [
                'nama_resep' => 'Sate Ayam Madura',
                'category_id' => 1, // Nusantara
                'deskripsi' => 'Sate ayam dengan bumbu kacang kental khas Madura yang gurih, manis, dan menggugah selera.',
                'bahan' => json_encode([
                    '500g Daging Ayam Fillet', 
                    '100g Kacang Tanah (Goreng)', 
                    '5 siung Bawang Merah', 
                    '3 siung Bawang Putih', 
                    'Kecap Manis secukupnya',
                    'Jeruk Limau'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Potong daging ayam bentuk dadu.', 
                    '2. Haluskan kacang tanah, bawang merah, bawang putih, cabe merah.', 
                    '3. Tumis bumbu hingga harum, tambahkan air dan kecap.', 
                    '4. Tusuk ayam dengan tusuk sate.', 
                    '5. Bakar sate hingga matang sambil diolesi bumbu.',
                    '6. Sajikan dengan sisa bumbu kacang.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1555126634-323283e090fa?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Rendang Daging Sapi',
                'category_id' => 1, // Nusantara
                'deskripsi' => 'Masakan daging asli Indonesia yang dimasak lama dengan santan dan rempah-rempah.',
                'bahan' => json_encode([
                    '1kg Daging Sapi', 
                    '1 liter Santan Kental', 
                    'Serai, Daun Kunyit, Daun Jeruk', 
                    'Bumbu Halus (Bawang, Cabe, Jahe, Lengkuas)'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Potong daging sapi sesuai selera.', 
                    '2. Masak santan dengan bumbu halus dan daun-daunan.', 
                    '3. Masukkan daging, masak dengan api kecil sambil diaduk.', 
                    '4. Masak terus hingga santan mengering dan keluar minyak (sekitar 3-4 jam).',
                    '5. Angkat dan sajikan.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1617387227488-8255b76c8c43?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Soto Betawi',
                'category_id' => 1, // Nusantara
                'deskripsi' => 'Soto bersantan yang gurih dengan isian daging dan jeroan sapi, khas Jakarta.',
                'bahan' => json_encode([
                    '500g Daging Sapi', 
                    '200ml Santan', 
                    '200ml Susu Cair (opsional)', 
                    'Kentang & Tomat',
                    'Emping'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Rebus daging hingga empuk, potong-potong.', 
                    '2. Tumis bumbu halus (bawang, kemiri, ketumbar).', 
                    '3. Masukkan bumbu ke kaldu daging, tambahkan santan dan susu.', 
                    '4. Masak hingga mendidih.', 
                    '5. Sajikan dengan pelengkap.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1572656303110-cee010df02fb?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Dimsum Siomay Ayam',
                'category_id' => 2, // Chinese
                'deskripsi' => 'Camilan kukus ala Tiongkok yang lembut, terbuat dari daging ayam dan udang.',
                'bahan' => json_encode([
                    '300g Daging Ayam Giling', 
                    '100g Udang Cincang', 
                    'Kulit Pangsit', 
                    'Tepung Tapioka',
                    'Wortel Parut (Topping)'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Campur ayam, udang, tapioka, dan bumbu.', 
                    '2. Ambil selembar kulit pangsit, isi dengan adonan.', 
                    '3. Bentuk seperti mangkuk, beri topping wortel.', 
                    '4. Kukus selama 20-25 menit.',
                    '5. Sajikan dengan saus sambal.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1496116218417-1a781b1c416c?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Ayam Kung Pao',
                'category_id' => 2, // Chinese
                'deskripsi' => 'Tumisan ayam khas Sichuan dengan cita rasa pedas, manis, dan kacang mete renyah.',
                'bahan' => json_encode([
                    '400g Ayam Fillet', 
                    '50g Kacang Mete Goreng', 
                    'Paprika & Cabe Kering', 
                    'Saus Tiram, Kecap Asin, Minyak Wijen'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Marinasi ayam dengan kecap asin.', 
                    '2. Tumis cabe kering dan bawang putih.', 
                    '3. Masukkan ayam, masak hingga berubah warna.', 
                    '4. Tambahkan paprika dan saus racikan.', 
                    '5. Masukkan kacang mete, aduk rata lalu angkat.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1525755662778-989d0524087e?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Classic Beef Burger',
                'category_id' => 3, // Western
                'deskripsi' => 'Burger daging sapi juicy dengan keju meleleh dan sayuran segar.',
                'bahan' => json_encode([
                    'Burger Bun', 
                    '200g Daging Sapi Giling', 
                    'Keju Slice (Cheddar)', 
                    'Selada, Tomat, Bawang Bombay',
                    'Saus BBQ & Mayones'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Bentuk daging giling menjadi patty, bumbui garam lada.', 
                    '2. Panggang patty di teflon hingga matang, beri keju di atasnya.', 
                    '3. Panggang roti sebentar.', 
                    '4. Susun roti, sayuran, patty, dan saus.',
                    '5. Sajikan.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Spaghetti Bolognese',
                'category_id' => 3, // Western
                'deskripsi' => 'Pasta Italia klasik dengan saus daging tomat yang kaya rasa.',
                'bahan' => json_encode([
                    '200g Spaghetti', 
                    '150g Daging Sapi Cincang', 
                    'Saus Tomat / Pasta Tomat', 
                    'Bawang Bombay & Bawang Putih',
                    'Oregano & Basil'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Rebus spaghetti hingga al dente, tiriskan.', 
                    '2. Tumis bawang, masukkan daging cincang hingga matang.', 
                    '3. Masukkan saus tomat dan bumbu rempah, masak hingga mengental.', 
                    '4. Campurkan saus ke spaghetti atau siram di atasnya.',
                    '5. Taburi keju parmesan.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1622973536968-3ead9e780960?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Caesar Salad',
                'category_id' => 3, // Western
                'deskripsi' => 'Salad segar dengan selada romain, crouton renyah, dan dressing creamy.',
                'bahan' => json_encode([
                    'Selada Romaine', 
                    'Roti Tawar (untuk Crouton)', 
                    'Keju Parmesan', 
                    'Dada Ayam Panggang (opsional)',
                    'Dressing Caesar (Mayo, Lemon, Bawang Putih)'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Potong roti dadu, panggang hingga kering (crouton).', 
                    '2. Cuci bersih selada, sobek-sobek.', 
                    '3. Aduk selada dengan dressing caesar.', 
                    '4. Beri topping ayam panggang, crouton, dan keju parmesan.',
                    '5. Sajikan segera.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1550304943-4f24f54ddde9?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Klepon Gula Merah',
                'category_id' => 4, // Dessert
                'deskripsi' => 'Kue tradisional bola ketan hijau berisi gula merah cair dengan taburan kelapa.',
                'bahan' => json_encode([
                    '200g Tepung Ketan', 
                    'Pasta Pandan', 
                    'Gula Merah (sisir halus)', 
                    'Kelapa Parut (kukus)'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Uleni tepung ketan dengan air hangat dan pasta pandan hingga kalis.', 
                    '2. Ambil sedikit adonan, pipihkan, isi gula merah, bulatkan.', 
                    '3. Rebus di air mendidih hingga mengapung.', 
                    '4. Gulingkan di kelapa parut.',
                    '5. Siap dinikmati saat gula masih cair.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1669299498263-d3493e800c19?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
            [
                'nama_resep' => 'Japanese Fluffy Pancake',
                'category_id' => 4, // Dessert
                'deskripsi' => 'Pancake super lembut dan tebal ala Jepang yang bergoyang saat disentuh.',
                'bahan' => json_encode([
                    '2 butir Telur (pisahkan kuning & putih)', 
                    '30g Tepung Terigu', 
                    '20ml Susu Cair', 
                    'Gula Pasir',
                    'Vanilli'
                ]),
                'langkah_langkah' => json_encode([
                    '1. Campur kuning telur, susu, tepung, vanilli.', 
                    '2. Kocok putih telur dan gula hingga kaku (meringue).', 
                    '3. Campur adonan kuning ke meringue secara bertahap (teknik lipat).', 
                    '4. Masak di teflon anti lengket dengan api sangat kecil, tutup pan.', 
                    '5. Balik hati-hati, masak hingga matang.'
                ]),
                'image' => 'https://images.unsplash.com/photo-1598214886806-c87b84b7078b?w=800&q=80',
                'created_at' => now(), 'updated_at' => now(),
            ],
        ];

        DB::table('recipes')->insert($recipes);
    }
}
