{{-- resources/views/contact.blade.php --}}

@extends('layouts.nosearch')

@section('title', 'Contact Us')

@section('content')
<div class="container"><br>
    <h1>Contact Us</h1><br><br>
    <p>We value your feedback, inquiries, and suggestions. Please don't hesitate to get in touch with us using the contact information below:</p>

    <br> <p><strong>Customer Support:</strong><br>
    For assistance with your orders, product information, or any other inquiries, our dedicated customer support team is here to help you. You can reach them at <a href="mailto:email@example.com">knowledgeHub@gmail.com</a></p>

    <br> <p><strong>General Inquiries:</strong><br>
    If you have general questions, feedback, or partnership opportunities, please email us at <a href="mailto:info@example.com">knowledgeHub@gmail.com</a>.</p>

    <br><p><strong>Address:</strong><br>
    [KnowledgeHub]<br>
    [FEUP]<br>
    [Porto]</p><br>

    <p><strong>Working Hours:</strong><br>
    Our team is available to assist you during the following hours:<br>
    Monday - Friday: 9:00 AM - 6:00 PM (Local Time)<br>

    Saturday - Sunday: Closed</p>

    <p>We strive to respond to all inquiries within 24-48 hours. Your satisfaction is important to us, and we look forward to hearing from you!</p>

    <a href="{{ url('/allquestions') }}" class="button back-button">Back</a>

</div>
@endsection
