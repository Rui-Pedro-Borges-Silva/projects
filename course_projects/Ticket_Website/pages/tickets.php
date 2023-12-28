<?php
    declare(strict_types = 1);

    require_once(__DIR__ . '/../utils/session.php');
    $session = new Session();

    require_once(__DIR__ . '/../database/connection.db.php');
    require_once(__DIR__ . '/../database/ticket.class.php');
    require_once(__DIR__ . '/../database/department.db.php');
    require_once(__DIR__ . '/../templates/footer.php');
    require_once(__DIR__ . '/../templates/header.php');
    require_once(__DIR__ . '/../templates/ticketTable.php');

    if (!$session->isLoggedIn()) {
        die(header('Location: ../pages/login.php'));
    }

    $db = getDatabaseConnection();

    // Get the filter parameters from the GET request
    $department = $_GET['department'] ?? NULL;
    $agent = $_GET['agent'] ?? NULL;
    $status = $_GET['status'] ?? NULL;
    $priority = $_GET['priority'] ?? NULL;
    $hashtag = $_GET['hashtag'] ?? NULL;

    // Pass the filter parameters into the getTickets function
    $tickets = Ticket::getTickets($db, 20, $department, $agent, $status, $priority, $hashtag);
    
    $agents =  User::getAgents($db, 20);
    $clients =  User::getClients($db, 20);
    $type = (User::getUser($db, $session->getId()))->type;
    $final = [];
    $flag = false;

    foreach ($tickets as $ticket) {
        $agentName = '';
        $clientName = '';
        $departmentName = '';
        $userName = '';
    
        // Find the matching agent name
        foreach ($agents as $agent) {
            if ($agent->id === $ticket->agentId) {
                $agentName = $agent->name;
                $flag = true;
                break;
            }
        }

        if($type !== 'client'){
            $user = User::getUser($db, $session->getId());
            if($user->id === $ticket->clientId){
                $userName = $user->name;
            }
        }
    
        // Find the matching client name
        foreach ($clients as $client) {
            if ($client->id === $ticket->clientId) {
                $clientName = $client->name;
                break;
            }
        }
    
        $departmentName = $ticket->type;
        $departmentName = $departmentName . " Department";

        if($flag){
            $clientName = $userName;
        }
    
        // Create a new ticket object with replaced agentId and clientId
        $newTicket = [
            'id' => $ticket->id,
            'name' => $ticket->name,
            'type' => $ticket->type,
            'status' => $ticket->status,
            'info' => $ticket->info,
            'priority' => $ticket->priority,
            'agentName' => $agentName,
            'departmentName' => $departmentName,
            'clientName' => $clientName
        ];
    
        // Add the new ticket to the final array
        if ($type === 'client') {
            if ($ticket->clientId === $session->getId()) {
                $final[] = $newTicket;
            }
        } else {
            $final[] = $newTicket;
        }
    }    

    
    drawHeader($session, $type);
    drawTicketTable($session, $final, $type);
    drawFooter();
?>