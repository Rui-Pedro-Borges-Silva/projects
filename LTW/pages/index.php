<?php
  declare(strict_types = 1);

  require_once(__DIR__ . '/../utils/session.php');
  $session = new Session();

  require_once(__DIR__ . '/../database/connection.db.php');
  require_once(__DIR__ . '/../templates/footer.php');
  require_once(__DIR__ . '/../templates/header.php');
  require_once(__DIR__ . '/../templates/authentication.php');
  require_once(__DIR__ . '/../utils/welcome.php');

  $db = getDatabaseConnection();
  drawInitialHeader($session);
  drawWelcome($session);
  drawLogOutButton();
  drawFooter();
?>