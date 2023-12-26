<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class ContactController extends Controller
{
    public function send(Request $request)
    {
        // Process the form submission and send an email, etc.

        // Redirect back to the previous page with a success message
        return redirect()->back()->with('success', 'Thank you for contacting us! We will be in touch soon.');
    }
}
