{{-- resources/views/profile.blade.php --}}

@extends('layouts.nosearch')

@if ($errors->any())
    <div>
        <ul>
            @foreach ($errors->all() as $error)
                <li>{{ $error }}</li>
            @endforeach
        </ul>
    </div>
@endif

@section('content')
<div class="container">
    <h1>{{ $user->name }}'s Profile</h1><br><br>


    <!-- UserFeed Section -->
 <div class="feed-box">
    <h2>My Feed</h2>
    @foreach ($feedItems as $feedItem)
        <div class="feed-item" id="feed-item-{{ $feedItem->id }}">
            <p>{{ $feedItem->content }}</p>
            <button class="edit-feed-item-btn" onclick="toggleEditModeFeed({{ $feedItem->id }})">Edit</button>
            <div class="edit-mode-feed" id="edit-mode-feed-{{ $feedItem->id }}" style="display: none;">
                <textarea class="edit-feed-content">{{ $feedItem->content }}</textarea>
                <button class="save-feed-item-btn" onclick="saveFeedItem({{ $feedItem->id }})">Save</button>
                <button class="cancel-edit-btn" onclick="toggleEditModeFeed({{ $feedItem->id }})">Cancel</button>
            </div>
        </div>
    @endforeach

    <!-- User Questions Section -->
    <div class="questions-box">
        <h2>My Questions</h2>
        @foreach ($userQuestions as $question)
            <div class="question-item" id="question-{{ $question->id }}">
                <h3>{{ $question->title }}</h3>
                <p>{{ $question->description }}</p>
                <button class="edit-question-btn" onclick="toggleEditMode({{ $question->id }})">Edit</button>
                <form action="/questiondelete" method="GET">
                    @csrf
                    <input type="hidden" name="_id" value="{{ $question['id'] }}">
                    <button type="submit" class="delete-question-btn" aria-label="Delete this question">Delete</button>
                </form>
                <!-- Edit mode elements -->
                <div class="edit-mode" id="edit-mode-{{ $question->id }}" style="display: none;">
                    <textarea class="edit-title" placeholder="Title">{{ $question->title }}</textarea>
                    <textarea class="edit-description" placeholder="Description">{{ $question->description }}</textarea>
                    <button class="save-question-btn" onclick="saveQuestion({{ $question->id }})">Save</button>
                    <button class="cancel-edit-btn" onclick="toggleEditMode({{ $question->id }})">Cancel</button>
                </div>
            </div>
        @endforeach
    </div>

     <!-- User Answers Section -->
     <div class="answers-box">
        <h2>My Answers</h2>
        @foreach ($userAnswers as $answer)
            <div class="answer-item" id="answer-{{ $answer->id }}">
                <p>{{ $answer->text }}</p>
                <button class="edit-answer-btn" onclick="toggleEditModeAnswer({{ $answer->id }})">Edit</button>
                <!-- Edit mode elements for answers -->
                <div class="edit-mode-answer" id="edit-mode-answer-{{ $answer->id }}" style="display: none;">
                    <textarea class="edit-answer-text" placeholder="Edit your answer">{{ $answer->text }}</textarea>
                    <button class="save-answer-btn" onclick="saveAnswer({{ $answer->id }})">Save</button>
                    <button class="cancel-edit-btn" onclick="toggleEditModeAnswer({{ $answer->id }})">Cancel</button>
                </div>
                <form action="/answerdelete" method="GET">
                    @csrf
                    <input type="hidden" name="_id" value="{{ $answer['id'] }}">
                    <button type="submit" class="delete-question-btn" aria-label="Delete this question">Delete</button>
                </form>
            </div>
        @endforeach
    </div>
    <a href="{{ url('/allquestions') }}" class="button back-button">Back</a>


    <br><br><br><br><br><br>

    <div class="container">
         <div class="edit-profile-section" style="background: #fff; border: 1px solid #ddd; padding: 20px; margin-top: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
            <br><h1>Edit Profile</h1><br>

    
        <!-- Edit Username Section -->
        <div class="username-edit-section">
            <h2>Edit Username</h2>
            <form action="{{ route('update-username') }}" method="POST">
                @csrf
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" value="{{ $user->name }}" required>
                </div>
                <button type="submit" class="btn btn-primary">Update Username</button>
            </form>
        </div>

         <!-- Edit Email Section -->
         <div class="email-edit-section">
            <h2>Edit Email</h2>
            <form action="{{ route('update-email') }}" method="POST">
                @csrf
                <input type="email" name="email" value="{{ old('email', $user->email) }}" placeholder="Type new email" required>
                <input type="email" name="email_confirmation" placeholder="Write new email" required>
                <button type="submit">Update Email</button>
            </form>
        </div>
        

    <!-- Edit Password Section -->
    <div class="password-edit-section">
        <h2>Change Password</h2>
        <form action="{{ route('update-password') }}" method="POST">
            @csrf
            <input type="password" name="password" placeholder="Write new password" required>
            <input type="password" name="password_confirmation" placeholder="Confirm new password" required>
            <button type="submit">Change Password</button>
        </form>
    </div>    
    </div>

  <!-- Delete Account Button -->
  <div class="delete-account-section">
    <form action="{{ route('delete-account') }}" method="POST" onsubmit="return confirm('Are you sure you want to delete your account? This action cannot be undone.');">
        @csrf
        <button type="submit" class="btn btn-danger">Delete Account</button>
    </form>
</div>
@endsection

<script>
    function toggleEditMode(questionId) {
        let editModeDiv = document.getElementById(`edit-mode-${questionId}`);
        editModeDiv.style.display = editModeDiv.style.display === 'none' ? 'block' : 'none';
    }

    function saveQuestion(questionId) {
        const title = document.getElementById(`edit-mode-${questionId}`).querySelector('.edit-title').value;
        const description = document.getElementById(`edit-mode-${questionId}`).querySelector('.edit-description').value;

        // AJAX para atualizar a pergunta no servidor
        fetch(`/questions/update/${questionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': '{{ csrf_token() }}'
            },
            body: JSON.stringify({ title, description })
        })
        .then(response => response.json())
        .then(data => {
            // Atualiza a visualização da pergunta
            document.querySelector(`#question-${questionId} h3`).textContent = title;
            document.querySelector(`#question-${questionId} p`).textContent = description;
            toggleEditMode(questionId);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    
    function toggleEditModeAnswer(answerId) {
        let editModeDiv = document.getElementById(`edit-mode-answer-${answerId}`);
        editModeDiv.style.display = editModeDiv.style.display === 'none' ? 'block' : 'none';
    }

    function saveAnswer(answerId) {
        const text = document.getElementById(`edit-mode-answer-${answerId}`).querySelector('.edit-answer-text').value;

        // AJAX para atualizar a resposta no servidor
        fetch(`/answers/update/${answerId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': '{{ csrf_token() }}'
            },
            body: JSON.stringify({ text })
        })
        .then(response => response.json())
        .then(data => {
            // Atualiza a visualização da resposta
            document.querySelector(`#answer-${answerId} p`).textContent = text;
            toggleEditModeAnswer(answerId);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
</script>


<script>

    function toggleEditModeFeed(feedId) {
        let editModeDiv = document.getElementById(`edit-mode-feed-${feedId}`);
        editModeDiv.style.display = editModeDiv.style.display === 'none' ? 'block' : 'none';
    }

    function saveFeedItem(feedId) {
        const content = document.getElementById(`edit-mode-feed-${feedId}`).querySelector('.edit-feed-content').value;

        // AJAX to update the feed item on the server
        fetch(`/feed/update/${feedId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': '{{ csrf_token() }}'
            },
            body: JSON.stringify({ content })
        })
        .then(response => response.json())
        .then(data => {
            // Update the feed item view
            document.querySelector(`#feed-item-${feedId} p`).textContent = content;
            toggleEditModeFeed(feedId);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
</script>