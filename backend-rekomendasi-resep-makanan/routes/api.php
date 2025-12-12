<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\RecipeController;
use App\Http\Controllers\Api\CategoryController;

// PENTING: Pastikan tidak ada prefix '/api' lagi di sini
// Laravel otomatis menambahkan '/api' di depan semua route di file ini.
Route::get('/recipes', [RecipeController::class, 'index']);
Route::get('/recipes/{id}', [RecipeController::class, 'show']);
Route::post('/recipes', [RecipeController::class, 'store']);
Route::put('/recipes/{id}', [RecipeController::class, 'update']);
Route::delete('/recipes/{id}', [RecipeController::class, 'destroy']);

Route::get('/categories', [CategoryController::class, 'index']);
