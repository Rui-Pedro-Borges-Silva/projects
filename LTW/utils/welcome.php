<?php
function drawWelcome($session)
{
    ?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Problem Solvers</title>
        <link rel="stylesheet" href="../styles/welcome.css">
    </head>
    <body>
        <section class="welcome-page">
            <div class="welcome-content">
                <h1>Problem Solvers</h1>
                <a href="../pages/login.php" class="get-started-button">Get Started</a>
            </div>
        </section>
    </body>
    </html>
    <?php
}
?>
