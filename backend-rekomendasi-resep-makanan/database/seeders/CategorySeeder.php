<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class CategorySeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $categories = [
            ['nama_kategori' => 'Nusantara'],
            ['nama_kategori' => 'Chinese'],
            ['nama_kategori' => 'Western'],
            ['nama_kategori' => 'Dessert'],
            ['nama_kategori' => 'Lainnya'],
        ];

        foreach ($categories as $category) {
            \App\Models\Category::create($category);
        }
    }
}
