<?php

declare(strict_types=1);
?>

<?php function drawLogIn(Session $session)
{ ?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link rel="stylesheet" href="../styles/login.css">
        <link rel="stylesheet" href="../styles/global.css">
    </head>
    <body>
        <section class="login-page">
                <section class ="right-login-background">
                <form class="login-form" action="../actions/action_login.php" method="post">
                    <h2><a href = "../pages/index.php"> Problem Solvers </a></h2>
                    <h3>Login</h3>
                    <input type="text" name="username" placeholder='Username'>
                    <input type="password" name="password" placeholder='Password'>
                    <?php
                    // Display error messages
                    $messages = $session->getMessages();
                    foreach ($messages as $message) {
                        if ($message['type'] === 'error') {
                            echo '<p class="error">' . htmlspecialchars($message['text']) . '</p>';
                        }
                    }
                    // Clear messages
                    $session->clearMessages();
                    ?>
                    <input type="submit" value="Log in">
                    <a href="./signup.php"><button type="button">Register</button></a>
                </form>
                </section>
        </section>
    </body>
    </html>
<?php } ?>


<?php function drawSignUp(Session $session)
{ ?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link rel="stylesheet" href="../styles/signup.css">
    </head>
    <body>
        <section class="signup-page">
            <section class="left-signup-background">
                <form class="signup-form" action="../actions/action_signup.php" method="post">
                    <h2><a href="../pages/index.php">Problem Solvers</a></h2>
                    <h3>Sign Up</h3>
                    <input type="text" name="username" placeholder="Username">
                    <input type="email" name="email" placeholder="Email">
                    <input type="password" name="password" placeholder="Password">
                    <input type="password" name="confirm_password" placeholder="Confirm Password">
                    <input type="text" name="name" placeholder="Name">
                    <input type="text" name="type" placeholder="Role">
                    <input type="submit" value="Sign Up">
                    <p>Already have an account? <a href="./login.php">Log in</a></p>
                </form>
            </section>
        </section>
    </body>
    </html>
<?php } ?>

<?php function drawLogOutButton()
{
?>
    <a href= "../actions/action_logout.php"> Log Out </a>
<?php
}
?>