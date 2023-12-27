<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class QuestionModel extends Model
{
    use HasFactory;
    protected $table = 'question';
    public $timestamps = false;
    protected $fillable = ['title', 'description', 'authoruserid','selected_answer_id'];

    public function answers() {
        return $this->hasMany(AnswerModel::class, 'questionid');
    }

    public function tags()
    {
        return $this->belongsToMany(TagModel::class, 'questiontag', 'questionid', 'tagid');
    }
    public function selectedAnswer() {
        return $this->belongsTo(AnswerModel::class, 'selected_answer_id');
    }

    public function user() {
        return $this->belongsTo(User::class, 'authoruserid');
    }
    
}
