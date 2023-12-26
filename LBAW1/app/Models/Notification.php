<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    protected $primaryKey = 'NotiID';
    public $incrementing = false; // Set to false since 'NotiID' is not auto-incrementing
    protected $keyType = 'int'; // Set to 'int' if 'NotiID' is an integer

    protected $fillable = [
        'text', 
        'type', 
        'is_read', 
        'related_content_id', 
        'userid'
    ];

    public $timestamps = false;
}

