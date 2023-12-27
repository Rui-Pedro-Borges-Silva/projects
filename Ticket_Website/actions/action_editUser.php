<?php
    require_once('../utils/session.php');
    require_once('../database/connection.db.php');
    require_once('../database/user.db.php');

    $session = new Session();
    $db = getDatabaseConnection();

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $userId = (int)$_POST['userId'];
        $username = $_POST['username'];
        $email = $_POST['email'];
        $role = $_POST['role'];

        $user = User::getUser($db, $userId);

        $user->username = $username;
        $user->email = $email;
        $user->type = $role;

        $user->edit($db);
        header('Location: ../pages/users.php');
        exit();
    }
?>
