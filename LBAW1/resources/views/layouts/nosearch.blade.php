<!DOCTYPE html>
<html lang="{{ app()->getLocale() }}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSRF Token -->
    <meta name="csrf-token" content="{{ csrf_token() }}">

    <title>Knowledge Hub</title>

    <!-- Styles -->
    <link href="{{ url('css/milligram.min.css') }}" rel="stylesheet">
    <link href="{{ url('css/app.css') }}" rel="stylesheet">
    <script type="text/javascript">
        // Fix for Firefox autofocus CSS bug
        // See: http://stackoverflow.com/questions/18943276/html-5-autofocus-messes-up-css-loading/18945951#18945951
    </script>
    <script type="text/javascript" src={{ url('js/app.js') }} defer>
    </script>
</head>
<body>
<main>
    <header>
        <div class="main-header">
            <h1>Q&A</h1>
            <div>
                @if (Auth::check())
                    <a class="button" href="{{ url('/logout') }}"> Logout </a>
                    <span><a href="{{ url('/profile') }}">{{ Auth::user()->name }}</a></span>
                @endif
            </div>
        </div>
        <div class="side-header">
            <a href="{{ url('/') }}"><h1>KnowledgeHub</h1></a>
        </div>
    </header>

    <section id="content">
        @yield('content')
    </section>

    <footer>
        <div>
            <p>KnowledgeHub @2023</p>
            <div class="footer-nav">
                <a class="footer-link" href="{{ route('contact') }}">Contact Us</a>
                <a class="footer-link" href="{{ route('about') }}">About Us</a>
            </div>
        </div>
    </footer>
</main>
</body>
</html>
