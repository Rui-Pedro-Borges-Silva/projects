@extends('layouts.nosearch')

@section('content')
    <div class="form-container">
        <fieldset>
            <legend>Add Question</legend>
            <form action="/questions/store" method="POST">
                @csrf
                <div class="form-group">
                    <input type="text" name="title" placeholder="Enter Title">
                </div>
                <div class="form-group">
                    <input type="text" name="description" placeholder="Enter Description">
                </div>

                {{-- Checklist de Tags --}}
                <div class="form-group">
                    <label>Select Tags:</label><br>
                    @foreach ($tags as $tag)
                        <label>
                            <input type="checkbox" name="tags[]" value="{{ $tag->id }}"> {{ $tag->name }}
                        </label><br>
                    @endforeach
                </div>

                <div class="form-group">
                    <input type="submit" name="submit" value="Add">
                    <a href="{{ url('/allquestions') }}" class="button back-button">Back</a>
                </div>
            </form>
        </fieldset>
    </div>
@endsection
