<?php
  declare(strict_types = 1);

  require_once(__DIR__ . '/../utils/session.php');
  $session = new Session();

  require_once(__DIR__ . '/../database/department.db.php');
  require_once(__DIR__ . '/../database/connection.db.php');
  require_once(__DIR__ . '/../database/ticket.class.php');
  require_once(__DIR__ . '/../templates/footer.php');
  require_once(__DIR__ . '/../templates/header.php');
  require_once(__DIR__ . '/../templates/edit_ticket.php');

  $db = getDatabaseConnection();
  $ticketId = (int)$_GET['ticketId'];
  $ticket = Ticket::getTicket($db, $ticketId);
  $type = (User::getUser($db, $session->getId()))->type;
  $departments = Department::getDepartments($db, 20);
  drawHeader($session, $type);
  drawEditTicket($session, $type, $ticket, $departments);
  drawFooter();
?>