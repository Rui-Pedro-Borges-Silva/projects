<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserAnswerLike extends Model
{
    use HasFactory;
    protected $table = 'user_answer_likes';
    public $timestamps = false;
}


