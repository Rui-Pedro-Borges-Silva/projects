<?php
    declare(strict_types = 1);

    require_once(__DIR__ . '/../utils/session.php');
    $session = new Session();

    require_once(__DIR__ . '/../database/connection.db.php');
    require_once(__DIR__ . '/../database/department.db.php');
    require_once(__DIR__ . '/../templates/footer.php');
    require_once(__DIR__ . '/../templates/header.php');
    require_once(__DIR__ . '/../templates/userTable.php');

    if (!$session->isLoggedIn()) {
        die(header('Location: ../pages/login.php'));
    }

    $db = getDatabaseConnection();
    $users = User::getUsers($db, 20);
    $departments = Department::getDepartments($db, 20);
    $type = (User::getUser($db, $session->getId()))->type;
    $final = [];
    foreach($users as $user){
        $newUser = [
            'id' => $user->id,
            'name' => $user->name,
            'username' => $user->username,
            'email' => $user->email,
            'type' => $user->type,
        ];
        $final[] = $newUser;
    }

    drawHeader($session, $type);
    drawUsersTable($session, $final, $type);
    drawFooter();
?>