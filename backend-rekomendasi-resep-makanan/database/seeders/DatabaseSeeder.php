<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    public function run(): void
    {
        // Seed categories first
        $this->call(CategorySeeder::class);
        
        // Then seed recipes
        $this->call(RecipeSeeder::class);
    }
}
