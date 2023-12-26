<?php
require_once('../utils/session.php');
require_once('../database/connection.db.php');
require_once('../database/ticket.class.php');

$session = new Session();
$db = getDatabaseConnection();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Retrieve form data
    $ticketId = $_POST['ticketId'];
    $ticketName = $_POST['name'];
    $ticketType = $_POST['type'];
    $ticketInfo = $_POST['info'];
    $ticketPriority = $_POST['priority'];

    $ticket = Ticket::getTicket($db, $ticketId);

    if ($ticket !== null) {
        // Update the ticket information
        $ticket->name = $ticketName;
        $ticket->type = $ticketType;
        $ticket->info = $ticketInfo;
        $ticket->priority = $ticketPriority;

        // Save the updated ticket
        $ticket->save($db);

        // Redirect to the ticket details page or display a success message
        header('Location: ../pages/tickets.php');
        exit();
    } else {
        // Ticket not found, display an appropriate message or take necessary action
    }
}
?>
