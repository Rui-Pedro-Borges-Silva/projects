<?php
require_once('../utils/session.php');
require_once('../database/connection.db.php');
require_once('../database/user.db.php');

$session = new Session();
$db = getDatabaseConnection();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST["username"];
    $password = $_POST["password"]; // You should hash this

    if(!preg_match('/^[a-zA-Z0-9]{2,20}$/', $username)) {
        $session->addMessage('error', 'Username must be between 2 and 20 characters long and cannot be empty');
        die(header('Location: /'));
    }
    if(!preg_match('/^[a-zA-Z0-9!\?]{2,20}$/', $password)) {
        $session->addMessage('error', 'Password must be between 2 and 20 characters long and contain only letters and numbers and !? and cannot be empty');
        die(header('Location: /'));
    }
    $user = User::login($db, $username, $password);
    
    if ($user) {
        // Login successful, start a session and redirect
        $session->login($user);
        header('Location: ../pages/tickets.php');
        exit();
    } else {
        // Login failed, redirect back to the login page with an error message
        $session->addMessage('error', 'Invalid username or password');
        header('Location: ../pages/login.php');
        exit();
    }
}
?>
