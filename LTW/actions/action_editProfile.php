<?php
    require_once('../utils/session.php');
    require_once('../database/connection.db.php');
    require_once('../database/user.db.php');

    $session = new Session();
    $db = getDatabaseConnection();

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        // Retrieve form data
        $name = $_POST['name'];
        $username = $_POST['username'];
        $email = $_POST['email'];
        $currentPassword = $_POST['current_password'];
        $newPassword = $_POST['new_password'];
        $confirmPassword = $_POST['confirm_password'];

        $user = User::getUser($db, $session->getId());

        if ($user !== null) {
            // Update the user's profile information
            $user->name = $name;
            $user->username = $username;
            $user->email = $email;

            // Check if the user wants to change their password
            if (!empty($newPassword) && $newPassword === $confirmPassword) {
                // Save the updated user information with the new password
                $user->save($db, $newPassword);
            } else {
                // Save the updated user information without changing the password
                $user->save($db);
            }

            // Redirect to the profile page or display a success message
            header('Location: ../pages/profile.php');
            exit();
        } else {
            // User not found, display an appropriate message or take necessary action
        }
    }
?>
