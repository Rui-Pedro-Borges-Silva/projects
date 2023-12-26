{{-- resources/views/about.blade.php --}}

@extends('layouts.nosearch')

@section('title', 'About Us')

@section('content')
<div class="container">
    <h1>About Us</h1><br><br>
    <p>Welcome to our Q&A platform – a place where curiosity is rewarded and a wealth of knowledge grows with every exchange. Our mission is to create a community where anyone can come to learn and share their insights. Whether you're looking to answer your deepest questions or just want to explore the minds of others, you've come to the right place.</p>
    <br>
    <p>Our team is made up of passionate experts and curious minds who believe in the power of sharing knowledge. We're committed to providing an environment where all users can freely ask questions and provide answers in a respectful and collaborative manner.</p>
    <br>
    <p>If you have any questions about our platform or how to get involved, don't hesitate to get in touch. Happy questioning!</p>
    <a href="{{ url('/allquestions') }}" class="button back-button">Back</a>
</div>
@endsection
