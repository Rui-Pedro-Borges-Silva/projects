@extends('layouts.app')

@section('content')

    <div class="all-questions-add-button">
        <h1>All Questions</h1>
        @if (Auth::check() && (Auth::user()->role === 'admin'))
        <a href="{{ url('/admin/users') }}" class="button">All Users</a>
        @endif

        @if (Auth::check() && (Auth::user()->role === 'moderator' || Auth::user()->role === 'admin'))
            <a href="{{ url('/createtags') }}" class="button">create tags</a>
        @endif

        <a href="{{ url('/questions') }}" class="button">Ask Question</a>
    </div>

    <select id="question-filter" onchange="location = this.value;">
        <option value="{{ url('/') }}">All Questions</option>
        <option value="{{ route('top_questions') }}">Top Questions</option>
    </select>

    <div class="questions">
        @foreach($QuestionModel as $ques)
            <fieldset>
            @if(auth()->check() && (auth()->user()->role === 'moderator' || auth()->user()->role === 'admin'))
                <form action="/questiondelete" method="GET">
                    @csrf
                    <input type="hidden" name="_id" value="{{ $ques['id'] }}">
                    <button type="submit" class="delete-question-btn" aria-label="Delete this question">Delete</button>
                </form>
                <button class="btn btn-primary" onclick="toggleTagForm({{ $ques->id }})">Edit Tags</button>

                    <form action="/edit-tags" method="POST" id="tagForm{{ $ques->id }}" style="display: none;">
                        @csrf
                        <input type="hidden" name="question_id" value="{{ $ques->id }}">

                        <div class="form-group">
                            <label for="tags">Edit Tags:</label><br>
                            @foreach($allTags as $tag)
                                @php
                                    $tagId = $tag->id;
                                    $isChecked = in_array($tagId, $ques->tags->pluck('id')->toArray());
                                @endphp

                                <input type="checkbox" name="tags[]" value="{{ $tagId }}" {{ $isChecked ? 'checked' : '' }}>
                                {{ $tag->name }}<br>
                            @endforeach
                        </div>

                        <button type="submit" class="btn btn-primary">Save Tags</button>
                    </form>

                @endif



                <p>Title</p>
                <a href="{{ route('questions.show', $ques->id) }}">{{ $ques->title }}</a>
                <p>Question</p>
                <li>{{ $ques->description }}</li>

                <p>Tags:</p>
                @foreach($ques->tags as $tag)
                    <a href="#{{ $tag->name }}">#{{ $tag->name }}</a>
                @endforeach


                <p>Answers:</p>
                
                @php
            $sortedAnswers = $ques->answers->sortByDesc(function ($answer) use ($ques) {
            return $ques->selected_answer_id == $answer->id;
            });
            $answerCount = 0;
            @endphp

        @foreach($sortedAnswers as $answer)
        @if ($answerCount < 2)

        <li class="{{ $ques->selected_answer_id == $answer->id ? 'right-answer' : '' }}">
                    {{ $answer->text }}
                </li>
                @if(auth()->check() && auth()->id() == $ques->authoruserid)
                    <form action="{{ url('/questions/'.$ques->id.'/answers/'.$answer->id.'/select-right') }}" method="POST">
                        @csrf
                        <button type="submit">Correct</button>
                    </form>
                @endif

                @if(auth()->check() && (auth()->user()->role === 'moderator' || auth()->user()->role === 'admin'))
                <form action="/answerdelete" method="GET">
                    @csrf
                    <input type="hidden" name="_id" value="{{ $answer['id'] }}">
                    <button type="submit" class="delete-answer-btn" aria-label="Delete this answer">Delete</button>
                </form>
                @endif

            LIKES: <span id="likes-count-answer-{{ $answer->id }}">{{ $answer->votes }}</span>
        </li>

            @if(Auth::check() && Auth::user()->id != $answer->authoruserid)
                <form action="/answer/{{ $answer->id }}/like" method="POST" class="like-form">
                    @csrf
                    <button type="submit">Like</button>
                </form>
            @endif

            
        <hr>
        @php $answerCount++; @endphp
        @else
            @break <!-- Break out of the loop if 2 answers have been displayed -->
        @endif
        @endforeach

                <br><br>
                <form action="/allquestions" method="POST">
                    @csrf
                    <input type="text" name="text" placeholder="Enter Answer">
                    <input type="hidden" name="questionid" value="{{ $ques->id }}">
                    <input type="submit" value="Submit">
                </form>
            </fieldset>
        @endforeach
    </div>

    <script>
        // This script will navigate to the selected page when the dropdown changes
        document.getElementById('question-filter').value = window.location.href;
    </script>

@endsection

<script>
    function toggleTagForm(questionId) {
        const form = document.getElementById(`tagForm${questionId}`);
        if (form.style.display === 'none' || form.style.display === '') {
            form.style.display = 'block';
        } else {
            form.style.display = 'none';
        }
    }




</script>

<style>
    .right-answer {
        color: green;
        font-weight: bold;
    }
</style>
