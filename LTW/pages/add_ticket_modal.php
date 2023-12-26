<?php
  declare(strict_types = 1);

  require_once(__DIR__ . '/../utils/session.php');
  $session = new Session();

  require_once(__DIR__ . '/../database/connection.db.php');
  require_once(__DIR__ . '/../templates/footer.php');
  require_once(__DIR__ . '/../templates/header.php');
  require_once(__DIR__ . '/../templates/authentication.php');
  require_once(__DIR__ . '/../templates/add_ticket_modal.php');
  require_once(__DIR__ . '/../database/department.db.php');

  $db = getDatabaseConnection();
  $type = (User::getUser($db, $session->getId()))->type;
  $agents = User::getAgents($db, 20);
  $departments = Department::getDepartments($db, 20);
  drawHeader($session, $type);
  drawAddTicketModal($session, $type, $agents, $departments);
  drawFooter();
?>