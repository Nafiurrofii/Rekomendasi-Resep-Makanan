<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\RecipeController;
use App\Http\Controllers\Api\CategoryController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

// Route group untuk resep
Route::controller(RecipeController::class)->group(function () {
    Route::get('recipes', 'index');
    Route::get('recipes/{recipe}', 'show');
    Route::post('recipes', 'store'); // CREATE: menangani multipart/form-data
    Route::post('recipes/{recipe}', 'update'); // UPDATE: menggunakan POST untuk handle multipart/form-data
    Route::delete('recipes/{recipe}', 'destroy'); // DELETE
});

Route::get('categories', [CategoryController::class, 'index']);

// Auth Routes
use App\Http\Controllers\Api\AuthController;
Route::post('register', [AuthController::class, 'register']);
Route::get('register', function() {
    return response()->json(['message' => 'Method Not Allowed. Use POST.'], 405);
});
Route::post('login', [AuthController::class, 'login']);
Route::get('login', function() {
    return response()->json(['message' => 'Unauthorized'], 401);
})->name('login');
Route::post('logout', [AuthController::class, 'logout'])->middleware('auth:sanctum');
