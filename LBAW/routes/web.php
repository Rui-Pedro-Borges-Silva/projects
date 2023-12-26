<?php
namespace App\Http\Controllers;
use App\Models\User;
use Illuminate\Support\Facades\Route;

use App\Http\Controllers\CardController;


use App\Http\Controllers\Auth\LoginController;
use App\Http\Controllers\Auth\RegisterController;

use App\Http\Controllers\AnswersController;


/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

// Home
Route::redirect('/', '/allquestions');


// Authentication
Route::controller(LoginController::class)->group(function () {
    Route::get('/login', 'showLoginForm')->name('login')->middleware('guest');
    Route::post('/login', 'authenticate')->middleware('guest');
    Route::get('/logout', 'logout')->name('logout');
});

Route::controller(RegisterController::class)->group(function () {
    Route::get('/register', 'showRegistrationForm')->name('register')->middleware('guest');
    Route::post('/register', 'register')->middleware('guest');
});

Route::controller(QuestionController::class)->group(function () {
    Route::get('/questions', 'create')->middleware('auth');
    Route::post('/questions/store', 'store')->middleware('auth');
    Route::get('/questiondelete', 'deletequestion')->middleware('auth'); // Only authenticated users can delete questions
    Route::get('/answerdelete', 'deleteanswer')->middleware('auth'); // Only authenticated users can delete answers
    Route::post('/edit-tags', 'updatetags')->middleware('adminOrModerator');
});


Route::controller(QuestionController::class)->group(function () {
    Route::get('/allquestions', 'show');
});

Route::get('/questions', [QuestionController::class, 'create'])->middleware('auth');;

Route::controller(AnswersController::class)->group(function () {
    Route::post('/allquestions', 'createans'); // Only authenticated users can create answers
});

Route::get('/profile', [UserController::class, 'showProfile'])->middleware('auth');

Route::get('/search', 'App\Http\Controllers\QuestionController@search')->name('search');


Route::get('/top-questions', 'App\Http\Controllers\QuestionController@topQuestions')->name('top_questions');

Route::post('/questions/update/{id}', [QuestionController::class, 'update'])->name('questions.update');

Route::post('/answers/update/{id}', [AnswersController::class, 'update'])->middleware('auth')->name('answers.update');

Route::post('/feed/update/{id}', [UserController::class, 'updateFeedItem'])->middleware('auth')->name('feed.update');

Route::post('/update-username', [UserController::class, 'updateUsername'])->middleware('auth')->name('update-username');

Route::post('/update-email', [UserController::class, 'updateEmail'])->middleware('auth')->name('update-email');

Route::post('/update-password', [UserController::class, 'updatePassword'])->middleware('auth')->name('update-password');

Route::post('/delete-account', [UserController::class, 'deleteAccount'])->middleware('auth')->name('delete-account');

Route::view('/contact', 'contact')->name('contact');

Route::post('/contact', [ContactController::class, 'send'])->name('contact.send');

Route::view('/about', 'about')->name('about');

Route::get('/admin/users', [UserController::class, 'listAllUsers'])->middleware('admin');

Route::get('/createtags', [UserController::class, 'create_tags'])->middleware('adminOrModerator');

Route::post('/questions/{question}/answers/{answer}/select-right', [QuestionController::class, 'selectRightAnswer'])
     ->middleware('auth');


// Notifications
Route::post('/answer/{id}/like', [AnswersController::class, 'like'])->middleware('auth');

Route::get('/notifications', [UserController::class, 'showNotifications'])->middleware('auth');


Route::get('/questions/{id}', [QuestionController::class, 'showQuestion'])->name('questions.show');

Route::post('/add-tag', [UserController::class, 'addTag'])->name('add_tag')->middleware("adminOrModerator");

Route::delete('/admin/users/{user}', [UserController::class, 'destroy'])->name('admin.users.destroy')->middleware('admin');

Route::post('/questions/{question}/answers/{answer}/select-right', [QuestionController::class, 'selectRightAnswer'])->middleware('authorOnly');
