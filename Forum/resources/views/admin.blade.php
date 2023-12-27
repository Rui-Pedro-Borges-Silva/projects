@extends('layouts.nosearch')

@section('content')
    <div id="content">
        <h1>All Users</h1>
        <div class="questions-box">
            @forelse ($users as $user)
                <div class="question-item" style="display: flex; justify-content: space-between; align-items: center;">
                
                    <div>
                        <p><strong>Name:</strong> {{ $user->name }}</p>
                        <p><strong>Email:</strong> {{ $user->email }}</p>
                    </div>

                    <div style="display: flex; align-items: center;">
                        {{-- Verificar se o usuário não é um admin antes de mostrar a caixa de seleção e o botão de exclusão --}}
                        @if ($user->role !== 'admin')
                            <form action="{{ route('admin.users.destroy', $user->id) }}" method="POST" style="margin: 0;">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="btn btn-danger">Delete</button>
                            </form>
                        @endif
                    </div>
                </div>
            @empty
                <p>No users found.</p>
            @endforelse
        </div>
        <a href="{{ url('/allquestions') }}" class="button back-button">Back</a>

    </div>

@endsection
