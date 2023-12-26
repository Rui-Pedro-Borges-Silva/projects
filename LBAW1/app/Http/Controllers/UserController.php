<?php

namespace App\Http\Controllers;

use App\Models\TagModel;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use App\Models\User;
use App\Models\UserFeed;
use App\Models\QuestionModel;
use App\Models\AnswerModel;

class UserController extends Controller
{
    // Show the user's profile page
    public function showProfile()
    {
        $user = Auth::user(); // Get the authenticated user

        // Fetch the user's feed items
        $feedItems = $user->userFeed()->paginate(10);

        // Fetch the user's questions and answers with pagination
        $userQuestions = $user->questions()->paginate(10);
        $userAnswers = $user->answers()->paginate(10);

        return view('profile', [
            'user' => $user,
            'feedItems' => $feedItems,
            'userQuestions' => $userQuestions,
            'userAnswers' => $userAnswers,
        ]);
    }

    // Update a user's feed item
    public function updateFeedItem(Request $request, $id)
    {
        $feedItem = UserFeed::findOrFail($id);

        // Authorization check to ensure the user can only edit their feed items
        if ($feedItem->user_id != Auth::id()) {
            return response()->json(['message' => 'Unauthorized'], 403);
        }

        $feedItem->content = $request->input('content');
        $feedItem->save();

        return response()->json(['message' => 'Feed item updated successfully.']);
    }

    // Update a user's username
    public function updateUsername(Request $request)
    {
        $user = Auth::user();

        $request->validate([
            'username' => 'required|string|max:255|unique:users,name,' . $user->id,
        ]);

        $user->name = $request->input('username');
        $user->save();

        return redirect()->back()->with('success', 'Username updated successfully.');
    }

    // Update a user's email
    public function updateEmail(Request $request)
    {
        $user = Auth::user();

        $request->validate([
            'email' => 'required|string|email|max:255|unique:users,email,' . $user->id,
            'email_confirmation' => 'required|same:email',
        ]);

        $user->email = $request->input('email');
        $user->save();

        return redirect()->back()->with('success', 'Email updated successfully.');
    }

    // Update a user's password
    public function updatePassword(Request $request)
    {
        $user = Auth::user();

        $request->validate([
            'password' => 'required|string|min:8|confirmed',
        ]);

        $user->password = Hash::make($request->input('password'));
        $user->save();

        return redirect()->back()->with('success', 'Password updated successfully.');
    }

    // Delete a user's account
    public function deleteAccount()
    {
        try {
            $user = auth()->user(); // Get the authenticated user

            // Delete related models (questions, answers, user feed)
            QuestionModel::where('authoruserid', $user->id)->delete();
            AnswerModel::where('authoruserid', $user->id)->delete();
            UserFeed::where('userid', $user->id)->delete();

            // Delete the user
            $user->delete();

            // Logout the user after deletion
            auth()->logout();

            // Redirect to a given route with a success message
            return redirect('/allquestions')->with('success', 'Account deleted successfully.');
        } catch (\Exception $e) {
            // Handle the exception and redirect back with an error message
            return back()->with('error', 'There was a problem deleting your account.');
        }
    }

    // List all users (for admin)
    public function listAllUsers()
    {
        // Check if the authenticated user is an admin
        if (Auth::check() && Auth::user()->role === 'admin') {
            $users = User::all(); // Retrieve all users
            return view('admin', compact('users'));
        }

        return redirect('/'); // Redirect to the homepage if not an admin
    }

    // Display the form for creating tags
    public function create_tags()
    {
        $tags = TagModel::all(); // Replace TagModel with your actual tag model
        return view('create_tags', compact('tags'));
    }

    // Add a new tag
    public function addTag(Request $request): \Illuminate\Http\RedirectResponse
    {
        $request->validate([
            'tagName' => 'required|unique:tags,name', // Update validation rules as necessary
        ]);

        $tag = new TagModel(); // Replace with your actual tag model
        $tag->name = $request->input('tagName');
        $tag->save();

        return redirect()->back()->with('success', 'Tag added successfully.');
    }

    // Delete a user (for admin)
    public function destroy($id)
    {
        $user = User::findOrFail($id);

        // Delete related models (questions, answers, user feed)
        QuestionModel::where('authoruserid', $id)->delete();
        AnswerModel::where('authoruserid', $id)->delete();
        UserFeed::where('userid', $id)->delete();

        // Delete the user
        $user->delete();

        return redirect('/admin/users')->with('success', 'User deleted successfully.');
    }

    // Show user notifications
    public function showNotifications()
    {
        $userId = Auth::user()->id;
        $notifications = Notification::where('user_id', $userId)->get();

        return view('notifications', ['notifications' => $notifications]);
    }
}
