<?php
// delete_ticket.php

declare(strict_types = 1);

require_once(__DIR__ . '/../database/connection.db.php');
require_once(__DIR__ . '/../database/user.db.php');

// Check if the deleteUser value is provided in the POST request
if (isset($_POST['deleteUser'])) {
    $userId = $_POST['deleteUser'];

    // Delete the ticket
    $db = getDatabaseConnection();
    $result = User::deleteUser($db, (int)$userId);

    // Check the deletion result
    if ($result) {
        // User deleted successfully
        echo "User deleted successfully.";
    } else {
        // Failed to delete user
        echo "Failed to delete User.";
    }
}

// Redirect back to the users table page
header("Location: ../pages/users.php");
exit;
?>
