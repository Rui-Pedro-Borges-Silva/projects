<?php
require_once('../utils/session.php');
require_once('../database/connection.db.php');
require_once('../database/user.db.php');

$session = new Session();
$db = getDatabaseConnection();

function getGravatarUrl($email, $size = 80)
{
    $hash = md5(strtolower(trim($email)));
    return "https://www.gravatar.com/avatar/{$hash}?s={$size}&d=mp";
}


if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST["username"];
    $password = $_POST["password"]; // You should hash this
    $confirm_password = $_POST["confirm_password"];
    $email = $_POST["email"];
    $name = $_POST["name"];
    $type = $_POST["type"];

    $avatarUrl = getGravatarUrl($email);
    $image = $avatarUrl;

    // Check if passwords match
    if ($password !== $confirm_password) {
        $session->addMessage('error', 'Passwords do not match');
        header('Location: ../pages/signup.php');
        exit();
    }

    // Hash the password
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    // Attempt to create the user
    $user = User::create($db, $username, $hashed_password, $email, $name, $type, $image);
    
    if ($user) {
        // User creation successful, start a session and redirect
        $session->login($user);
        header('Location: ../pages/profile.php');
        exit();
    } else {
        // User creation failed, redirect back to the signup page with an error message
        $session->addMessage('error', 'Failed to create account');
        header('Location: ../pages/signup.php');
        exit();
    }
}
