<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Broadcasting\PrivateChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class AnswerLike implements ShouldBroadcast
{
    public $answerId;
    public $votes;
    public $likerId;
    public $authorId;
    public $message;

    public function __construct($answerId, $votes, $likerId, $authorId, $message)
    {
        $this->answerId = $answerId;
        $this->votes = $votes;
        $this->likerId = $likerId;
        $this->authorId = $authorId;
        $this->message = $message;
    }



    public function broadcastOn()
    {
        return new Channel('answer-channel');
    }

    public function broadcastAs()
    {
        return 'answer-liked';
    }
}
