@extends('layouts.app')

@section('content')
    <div class="question-details">
        <h2>{{ $question->title }}</h2>
        <p>{{ $question->description }}</p>

        <div class="tags">
            <h3>Tags</h3>
            @foreach($question->tags as $tag)
                <a href="#{{ $tag->name }}">#{{ $tag->name }}</a>
            @endforeach
        </div>

        <div class="answers">
            <h3>Answers</h3>
            
            @foreach($question->answers as $answer)
                <div id="answers-individual" class="{{ $question->selected_answer_id == $answer->id ? 'right-answer' : '' }}">
                    {{ $answer->text }}
                    <div>LIKES: <span id="likes-count-answer-{{ $answer->id }}">{{ $answer->votes }}</span></div>
                    @if(Auth::check() && Auth::user()->id != $answer->authoruserid)
                        <form action="/answer/{{ $answer->id }}/like" method="POST" class="like-form">
                            @csrf
                            <button type="submit">Like</button>
                        </form>
                    @endif
                    @if(auth()->check() && (auth()->user()->role === 'moderator' || auth()->user()->role === 'admin'))
                        <form action="/answerdelete" method="GET">
                            @csrf
                            <input type="hidden" name="_id" value="{{ $answer->id }}">
                            <button type="submit" class="delete-answer-btn" aria-label="Delete this answer">Delete</button>
                        </form>
                    @endif
                </div>
            @endforeach
        </div>

        @if(Auth::check())
            <form action="/allquestions" method="POST">
                @csrf
                <input type="text" name="text" placeholder="Enter Answer">
                <input type="hidden" name="questionid" value="{{ $question->id }}">
                <input type="submit" value="Submit">
            </form>
        @endif
    </div>

    <a href="{{ url('/allquestions') }}" class="button back-button">Back</a>
@endsection

<style>
    .right-answer {
        color: green;
        font-weight: bold;
    }
</style>
