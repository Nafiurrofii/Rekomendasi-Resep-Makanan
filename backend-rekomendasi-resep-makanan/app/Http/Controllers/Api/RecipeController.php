<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Recipe;
use Illuminate\Http\Request;

class RecipeController extends Controller
{
    public function index()
    {
        // Ambil semua data resep dengan relasi category
        $recipes = Recipe::with('category')->get();

        // Kembalikan dalam format JSON
        return response()->json([
            'status' => 'success',
            'data' => $recipes
        ], 200);
    }

    public function show($id)
    {
        $recipe = Recipe::with('category')->find($id);

        if (!$recipe) {
            return response()->json([
                'status' => 'error',
                'message' => 'Recipe not found'
            ], 404);
        }

        return response()->json([
            'status' => 'success',
            'data' => $recipe
        ], 200);
    }

    public function store(Request $request)
    {
        // Validasi input
        $validated = $request->validate([
            'nama_resep' => 'required|string|max:255',
            'category_id' => 'required|exists:categories,id',
            'deskripsi' => 'required|string',
            'bahan' => 'required|array',
            'langkah_langkah' => 'required|array',
            'gambar' => 'nullable|string'
        ]);

        // Buat resep baru - Laravel auto encode via $casts
        $recipe = Recipe::create([
            'nama_resep' => $validated['nama_resep'],
            'category_id' => $validated['category_id'],
            'deskripsi' => $validated['deskripsi'],
            'bahan' => $validated['bahan'],
            'langkah_langkah' => $validated['langkah_langkah'],
            'gambar' => $validated['gambar'] ?? ''
        ]);
        
        // Load category relationship
        $recipe->load('category');

        return response()->json([
            'status' => 'success',
            'message' => 'Recipe created successfully',
            'data' => $recipe
        ], 201);
    }

    public function update(Request $request, $id)
    {
        $recipe = Recipe::find($id);

        if (!$recipe) {
            return response()->json([
                'status' => 'error',
                'message' => 'Recipe not found'
            ], 404);
        }

        $validated = $request->validate([
            'nama_resep' => 'required|string|max:255',
            'category_id' => 'required|exists:categories,id',
            'deskripsi' => 'required|string',
            'bahan' => 'required|array',
            'langkah_langkah' => 'required|array',
            'gambar' => 'nullable|string'
        ]);

        $recipe->update([
            'nama_resep' => $validated['nama_resep'],
            'category_id' => $validated['category_id'],
            'deskripsi' => $validated['deskripsi'],
            'bahan' => $validated['bahan'],
            'langkah_langkah' => $validated['langkah_langkah'],
            'gambar' => $validated['gambar'] ?? $recipe->gambar
        ]);

        $recipe->load('category');

        return response()->json([
            'status' => 'success',
            'message' => 'Recipe updated successfully',
            'data' => $recipe
        ], 200);
    }

    public function destroy($id)
    {
        $recipe = Recipe::find($id);

        if (!$recipe) {
            return response()->json([
                'status' => 'error',
                'message' => 'Recipe not found'
            ], 404);
        }

        $recipe->delete();

        return response()->json([
            'status' => 'success',
            'message' => 'Recipe deleted successfully'
        ], 200);
    }
}
