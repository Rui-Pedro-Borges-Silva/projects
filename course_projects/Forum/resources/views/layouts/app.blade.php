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
    <script type="text/javascript" src={{ url('js/app.js') }} defer></script>
    <script src="https://js.pusher.com/7.0/pusher.min.js" defer></script>

    <script >

        document.addEventListener('DOMContentLoaded', function () {
            const pusher = new Pusher('1d79ab1e2db0eb80e604', {
                cluster: 'eu',
                encrypted: true
            });

            const channel = pusher.subscribe('answer-channel');

            channel.bind('answer-liked', function(data) {
                alert(data.message);

                // Use the correct property name as per your Pusher event data
                const likesCountElement = document.getElementById('likes-count-answer-' + data.answerId);
                if (likesCountElement) {
                    likesCountElement.textContent = data.votes;
                } else {
                    console.log('Element not found for answer ID:', data.answerId);
                }
            });


            const likeForms = document.querySelectorAll('.like-form');
            likeForms.forEach(form => {
                form.addEventListener('submit', function (e) {
                    e.preventDefault();
                    const formData = new FormData(this);
                    fetch(this.action, {
                        method: 'POST',
                        headers: {
                            'X-CSRF-TOKEN': '{{ csrf_token() }}',
                            'Accept': 'application/json'
                        },
                        body: formData
                    }).then(response => {
                        console.log('Liked successfully');
                    }).catch(error => {
                        console.error('Error:', error);
                    });
                });
            });
        });



        function toggleDropdown() {
            document.getElementById("notificationMenu").classList.toggle("show");
        }

        // Close the dropdown if the user clicks outside of it
        window.onclick = function(event) {
            if (!event.target.matches('.notification-dropdown button')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }

    </script>
    <style>
        /* Dropdown button */
        .notification-dropdown button {
            /* Styling for the dropdown button */
        }

        /* Dropdown content */
        .dropdown-content {
            display: none;
            /* Additional styling */
        }

        /* Show the dropdown */
        .show { display: block; }
    </style>
</head>
<body>
<main>
    <header>
        <div class="main-header">
            <h1>Q&A</h1>
            <div>
            @if (Auth::check())
    <a class="button" href="{{ url('/logout') }}">Logout</a>
    <span><a href="{{ url('/profile') }}">{{ Auth::user()->name }}</a></span>
            @else
    <a class="button" href="{{ url('/login') }}">Login</a>
            @endif


            </div>
        </div>
        <div class="side-header">
            <a href="{{ url('/') }}"><h1>KnowledgeHub</h1></a>
        </div>
        @if (Auth::check())
            <form action="{{ route('search') }}" method="GET">
                <div class="search">
                    <input type="text" name="query" placeholder="Search..." required>
                    <button type="submit">Search</button>
                </div>
            </form>
        @endif

        @if (Auth::check())
            <div class="notification-dropdown">
                <button onclick="toggleDropdown()">Notifications</button>
                <div id="notificationMenu" class="dropdown-content">
                    @forelse ($notifications ?? [] as $notification)
                        <a href="#">{{ $notification->text }}</a>
                    @empty
                        <a href="#">No notifications</a>
                    @endforelse
                </div>
            </div>
        @endif


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
