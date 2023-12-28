<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserFeed extends Model
{
    use HasFactory;
    protected $table = 'userfeed';

    protected $fillable = ['userid', 'content'];

    // Define the relationship to the User
    public function user()
    {
        return $this->belongsTo(User::class, 'userid');
    }

}
