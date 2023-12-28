<?php
// delete_ticket.php

declare(strict_types = 1);

require_once(__DIR__ . '/../database/connection.db.php');
require_once(__DIR__ . '/../database/ticket.class.php');

// Check if the deleteTicket value is provided in the POST request
if (isset($_POST['deleteTicket'])) {
    $ticketId = $_POST['deleteTicket'];

    // Delete the ticket
    $db = getDatabaseConnection();
    $result = Ticket::deleteTicket($db, (int)$ticketId);

    // Check the deletion result
    if ($result) {
        // Ticket deleted successfully
        echo "Ticket deleted successfully.";
    } else {
        // Failed to delete ticket
        echo "Failed to delete ticket.";
    }
}

// Redirect back to the ticket table page
header("Location: ../pages/tickets.php");
exit;
?>
