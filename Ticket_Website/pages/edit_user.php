<?php
  declare(strict_types = 1);

  require_once(__DIR__ . '/../utils/session.php');
  $session = new Session();

  require_once(__DIR__ . '/../database/connection.db.php');
  require_once(__DIR__ . '/../templates/footer.php');
  require_once(__DIR__ . '/../templates/header.php');
  require_once(__DIR__ . '/../templates/edit_user.php');

  $db = getDatabaseConnection();
  $userId = (int)$_GET['userId'];
  $type = (User::getUser($db, $session->getId()))->type;
  $user = User::getUser($db, $userId);
  drawHeader($session, $type);
  drawEditUser($session, $type, $user);
  drawFooter();
?>