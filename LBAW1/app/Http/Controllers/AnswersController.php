<?php

namespace App\Http\Controllers;

use App\Models\AnswerModel;
use App\Models\User;
use App\Models\Notification;
use App\Models\UserAnswerLike;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Pusher\Pusher;
use App\Events\AnswerLike;

class AnswersController extends Controller {

    // Function to create an answer
    function createans(Request $req) {
        // Check if the user is authenticated
        if (!Auth::check()) {
            return redirect('/login');
        }

        // Create a new answer instance
        $ans = new AnswerModel();
        $ans->text = $req->input('text');
        $ans->authoruserid = Auth::user()->id;
        $ans->questionid = $req->input('questionid');
        $ans->save();

        // Redirect the user based on their role
        if (Auth::user()->role === 'moderator') {
            return redirect('/allquestions');
        } else {
            return redirect('/allquestions');
        }
    }

    // Function to update an answer
    function update(Request $request, $id) {
        $answer = AnswerModel::findOrFail($id);
        $answer->text = $request->text;
        $answer->save();

        return response()->json(['message' => 'Answer updated successfully.']);
    }

    // Function to handle answer likes
    public function like($id) {
        $userId = Auth::user()->id;
        $like = UserAnswerLike::where('user_id', $userId)->where('answer_id', $id)->first();

        $answer = AnswerModel::find($id);

        // Check if the answer exists
        if (!$answer) {
            return back()->with('error', 'Answer not found.');
        }

        if ($like) {
            // Decrease the vote count and remove the like
            $answer->votes -= 1;
            $like->delete();
        } else {
            // Increase the vote count and record the like
            $answer->votes += 1;
            $like = new UserAnswerLike();
            $like->user_id = $userId;
            $like->answer_id = $id;
            $like->save();
        }

        // Message for the user who liked the answer
        $likerMessage = "You liked an answer!";

        // Check if the author of the answer is different from the liker
        if ($userId != $answer->authoruserid) {
            // Message for the author of the answer
            $authorMessage = "User {$userId} liked your answer: {$answer->text}";

            // Notify the author
            event(new AnswerLike($id, $answer->votes, $userId, $answer->authoruserid, $authorMessage));
        }

        // Notify the liker
        event(new AnswerLike($id, $answer->votes, $userId, $userId, $likerMessage));

        $answer->save();
        return back();
    }
}
