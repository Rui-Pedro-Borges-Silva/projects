<?php
    require_once('../utils/session.php');
    require_once('../database/connection.db.php');
    require_once('../database/ticket.class.php');
    require_once('../database/department.db.php');


    $session = new Session();
    $db = getDatabaseConnection();
    $agents = User::getAgents($db, 20);
    $departments = Department::getDepartments($db, 20);

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        // Retrieve form data
        $tickets = Ticket::getTickets($db, 10);

        $ticketName = $_POST["ticketName"];
        $type = $_POST["ticketType"]; 
        $status = 'Open';
        $info = $_POST["ticketInfo"];
        $priority = $_POST["priority"];  // Retrieve the selected priority value
        $agentName = $_POST["agent"];  // Retrieve the selected agent value
        $departmentId = NULL;
        $clientId = $session->getId();

        foreach($departments as $department){
            if($department->name === $type){
                $departmentId = $department->id;
            }
        }
    
        $agentId = null;
        foreach ($agents as $agent) {
            if ($agent->name === $agentName) {
                $agentId = $agent->id;
                break;
            }
        }
    
        $ticket = Ticket::create($db, $ticketName, $type, $status, $info, $priority, $agentId, $departmentId, $clientId);

        if ($ticket) {
            header('Location: ../pages/tickets.php');
            exit();
        } else {
            $session->addMessage('error', 'Failed to create ticket');
            header('Location: ../pages/add_ticket_modal.php');
            exit();
        }
    } else {
        header('Location: ../pages/add_ticket_modal.php');
        exit();
    }
?>
