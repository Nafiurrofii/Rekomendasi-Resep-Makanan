<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Recipe extends Model
{
    protected $fillable = [
        'nama_resep',
        'category_id',
        'deskripsi',
        'bahan',
        'langkah_langkah',
        'gambar',
    ];

    protected $casts = [
        'bahan' => 'array',
        'langkah_langkah' => 'array',
    ];
    
    public function category()
    {
        return $this->belongsTo(Category::class);
    }
}
