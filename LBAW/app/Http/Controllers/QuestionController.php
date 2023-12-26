<?php

namespace App\Http\Controllers;

use App\Models\AnswerModel;
use App\Models\QuestionModel;
use App\Models\TagModel;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class QuestionController extends Controller {
    // Display the form to create a new question
    function create() {
        $tags = TagModel::all();
        return view('QuestionForm', ['tags' => $tags]);
    }

    // Store a new question in the database
    function store(Request $req) {
        $question = new QuestionModel();
        $question->title = $req->input('title');
        $question->description = $req->input('description');
        $question->authoruserid = Auth::user()->id;
        $question->save();

        $tags = $req->input('tags', []); // Get selected tag IDs
        $question->tags()->sync($tags); // Associate tags with the question

        return redirect('/allquestions');
    }

    // Display questions along with their answers
    public function show() {
        $allTags = TagModel::all();
        $data = QuestionModel::with(['answers' => function ($query) {
            $query->orderBy('votes', 'desc');
        }])->get();
        return view('QAForm', ['QuestionModel' => $data, 'allTags' => $allTags]);
    }

    // Search for questions based on text and tags
    public function search(Request $request) {
        $searchTerm = $request->input('query');
        $terms = explode(' ', $searchTerm);

        $tagTerms = [];
        $textTerms = [];

        foreach ($terms as $term) {
            if (str_starts_with($term, '#')) {
                // Remove the '#' and prepare for tag search
                $tagTerms[] = str_replace('#', '', $term) . ':*';
            } else {
                $textTerms[] = $term;
            }
        }

        $formattedTextTerms = implode(' & ', $textTerms);

        // Search in 'question'
        $query = QuestionModel::query();

        if ($formattedTextTerms) {
            $query->whereRaw("textsearchable_index_col @@ to_tsquery('portuguese', ?)", [$formattedTextTerms]);
        }

        if ($tagTerms) {
            $query->whereHas('tags', function ($q) use ($tagTerms) {
                $q->whereRaw("to_tsvector('portuguese', name) @@ to_tsquery('portuguese', ?)", [implode(' | ', $tagTerms)]);
            });
        }

        $questions = $query->get();

        // Return the results to the view
        return view('search_results', ['questions' => $questions]);
    }

    // Display top questions based on answer count
    public function topQuestions() {
        $allTags = TagModel::all();
        $topQuestions = QuestionModel::withCount('answers')
                                    ->orderByDesc('answers_count')
                                    ->get();

        return view('top_questions', compact('topQuestions', 'allTags'));
    }

    // Update an existing question
    function update(Request $request, $id) {
        $question = QuestionModel::findOrFail($id);
        $question->title = $request->title;
        $question->description = $request->description;
        $question->save();

        return response()->json(['message' => 'Question updated successfully.']);
    }

    // Delete a question
    function deletequestion(Request $request) {
        $ques = QuestionModel::find($request->_id);
        $ques->delete();

        return redirect('/allquestions');
    }

    // Delete an answer
    function deleteanswer(Request $request) {
        $ques = AnswerModel::find($request->_id);
        $ques->delete();

        return redirect('/allquestions');
    }

    // Select the right answer for a question
    public function selectRightAnswer($questionId, $answerId) {
        $question = QuestionModel::where('id', $questionId)
                                 ->where('authoruserid', Auth::id())
                                 ->first();

        if ($question) {
            $question->selected_answer_id = $answerId;
            $question->save();
            return back()->with('success', 'Right answer selected.');
        }

        return back()->with('error', 'Unauthorized action.');
    }

    // Update tags for a question
    public function updateTags(Request $request) {
        // Get the question ID from the form data
        $questionId = $request->input('question_id');
        $question = QuestionModel::findOrFail($questionId);

        // Get the new tags from the form
        $newTags = $request->input('tags', []);

        // Update the tags for the question
        $question->tags()->sync($newTags);

        return redirect('/allquestions')->with('success', 'Tags updated successfully.');
    }

    // Show a specific question
    public function showQuestion($id) {
        $question = QuestionModel::with(['answers', 'tags', 'user'])->findOrFail($id);

        return view('question', compact('question'));
    }
}
