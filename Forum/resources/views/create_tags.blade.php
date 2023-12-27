@extends('layouts.app') {{-- Assuming you have a main layout file --}}

@section('content')

    <div class="container">
        <h1>Create and Manage Tags</h1>

        {{-- Display all tags --}}
        <div class="existing-tags">
            <h2>Existing Tags</h2>
            <ul>
                @foreach($tags as $tag)
                    <li>{{ $tag->name }}</li> {{-- Replace 'name' with your tag name attribute --}}
                @endforeach
            </ul>
        </div>

        {{-- Form to add a new tag --}}
        <div class="add-tag-form">
            <h2>Add a New Tag</h2>
            <form method="POST" action="{{ route('add_tag') }}"> {{-- Update the action route --}}
                @csrf
                <input type="text" name="tagName" placeholder="Enter tag name">
                <button type="submit">Add Tag</button>
            </form>
        </div>
    <a href="/allquestions" class="button">Back</a>
    </div>

@endsection
