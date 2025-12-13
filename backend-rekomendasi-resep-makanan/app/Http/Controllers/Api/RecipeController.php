<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Recipe;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;

class RecipeController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $recipes = Recipe::with('category')->latest()->get();
        return response()->json([
            'status' => 'success',
            'data' => $recipes,
        ]);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'nama_resep' => 'required|string|max:255',
            'category_id' => 'required|exists:categories,id',
            'deskripsi' => 'required|string',
            'bahan' => 'required|string',
            'langkah_langkah' => 'required|string',
            'image' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048',
            'image_url' => 'nullable|url'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'status' => 'error',
                'message' => 'Validation failed',
                'errors' => $validator->errors()
            ], 422);
        }

        // Custom check: Harus ada salah satu image atau image_url
        if (!$request->hasFile('image') && !$request->filled('image_url')) {
            return response()->json([
                'status' => 'error',
                'message' => 'Validation failed',
                'errors' => ['image' => ['Please upload an image file or provide a valid image URL.']]
            ], 422);
        }

        $imagePath = null;
        if ($request->hasFile('image')) {
            $imagePath = $request->file('image')->store('images', 'public');
        } elseif ($request->filled('image_url')) {
            $imagePath = $request->image_url;
        }

        $recipe = Recipe::create([
            'nama_resep' => $request->nama_resep,
            'category_id' => $request->category_id,
            'deskripsi' => $request->deskripsi,
            'bahan' => json_decode($request->bahan, true),
            'langkah_langkah' => json_decode($request->langkah_langkah, true),
            'image' => $imagePath,
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Recipe created successfully',
            'data' => $recipe->load('category'),
        ], 201);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $recipe = Recipe::with('category')->find($id);

        if ($recipe) {
            return response()->json([
                'status' => 'success',
                'data' => $recipe,
            ]);
        } else {
            return response()->json([
                'status' => 'error',
                'message' => 'Recipe not found',
            ], 404);
        }
    }

    public function update(Request $request, Recipe $recipe)
    {
        $validator = Validator::make($request->all(), [
            'nama_resep' => 'sometimes|required|string|max:255',
            'category_id' => 'sometimes|required|exists:categories,id',
            'deskripsi' => 'sometimes|required|string',
            'bahan' => 'sometimes|required|string',
            'langkah_langkah' => 'sometimes|required|string',
            'image' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048',
            'image_url' => 'nullable|url'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'status' => 'error',
                'message' => 'Validation failed',
                'errors' => $validator->errors()
            ], 422);
        }

        $imagePath = $recipe->image;

        if ($request->hasFile('image')) {
            if ($recipe->image && !str_starts_with($recipe->image, 'http')) {
                Storage::disk('public')->delete($recipe->image);
            }
            $imagePath = $request->file('image')->store('images', 'public');
        } elseif ($request->filled('image_url')) {
            // Jika user update dengan URL baru, dan gambar lama adalah file fisik, hapus file lama
            if ($recipe->image && !str_starts_with($recipe->image, 'http') && $recipe->image !== $request->image_url) {
                Storage::disk('public')->delete($recipe->image);
            }
            $imagePath = $request->image_url;
        }

        $recipe->update([
            'nama_resep' => $request->nama_resep,
            'category_id' => $request->category_id,
            'deskripsi' => $request->deskripsi,
            'bahan' => json_decode($request->bahan, true),
            'langkah_langkah' => json_decode($request->langkah_langkah, true),
            'image' => $imagePath,
        ]);

        return response()->json([
            'status' => 'success',
            'message' => 'Recipe updated successfully',
            'data' => $recipe->fresh()->load('category'),
        ]);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Recipe $recipe)
    {
        // 1. Hapus file gambar dari storage
        if ($recipe->image) {
            Storage::disk('public')->delete($recipe->image);
        }

        // 2. Hapus data resep dari database
        $recipe->delete();

        return response()->json([
            'status' => 'success',
            'message' => 'Recipe deleted successfully',
        ]);
    }
}
