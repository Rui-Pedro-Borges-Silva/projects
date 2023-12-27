<?php
  require_once('../templates/authentication.php');
  require_once('../utils/session.php');
  require_once('../templates/header.php');
  require_once('../templates/footer.php');
  require_once('../database/connection.db.php');

  $session= new Session();
  drawInitialHeader($session);
  drawSignUp($session);
  drawFooter();
?>