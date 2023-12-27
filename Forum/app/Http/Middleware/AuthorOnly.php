<?php

namespace App\Http\Middleware;

use App\Models\QuestionModel;
use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Symfony\Component\HttpFoundation\Response;

class AuthorOnly
{
    public function handle($request, Closure $next)
    {
        $questionId = $request->route('question'); // Adjust this if your route parameter is named differently
        $question = QuestionModel::find($questionId);

        if (!$question || $question->authoruserid != Auth::id()) {
            // If the question doesn't exist or the user is not the author
            return redirect('/allquestions')->with('error', 'Unauthorized access.');
        }

        return $next($request);
    }
}
