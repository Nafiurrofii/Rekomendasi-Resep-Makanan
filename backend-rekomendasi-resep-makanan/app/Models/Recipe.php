<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Storage;

class Recipe extends Model
{
    use HasFactory;

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'nama_resep',
        'category_id',
        'deskripsi',
        'bahan',
        'langkah_langkah',
        'image', // Tambahkan 'image'
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'bahan' => 'array',
        'langkah_langkah' => 'array',
    ];

    /**
     * Append an attribute to the model's JSON form.
     *
     * @var array
     */
    protected $appends = ['image_url'];

    /**
     * Get the full URL forthe recipe image.
     *
     * @return string|null
     */
    public function getImageUrlAttribute()
    {
        // Jika image null atau string kosong, return null
        if (empty($this->image) || trim($this->image) === '') {
            return null; 
        }

        // Jika sudah full URL (misal dummy data), kembalikan langsung
        if (str_starts_with($this->image, 'http')) {
            return $this->image;
        }

        // Jika path relatif, tambahkan base URL storage
        return asset('storage/' . $this->image);
    }

    public function category()
    {
        return $this->belongsTo(Category::class);
    }
}
