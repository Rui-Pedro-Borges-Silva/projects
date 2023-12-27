<?php
require_once('../templates/authentication.php');
require_once('../utils/session.php');
require_once('../templates/header.php');
require_once('../templates/footer.php');
require_once('../database/connection.db.php');

$session= new Session();
$db = getDatabaseConnection();

// Verify if user is logged in
if ($session->isLoggedIn()) {
    die(header('Location: ../pages/tickets.php'));
}
drawInitialHeader($session);
drawLogIn($session);
drawFooter();
?>
