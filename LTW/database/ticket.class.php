<?php
  declare(strict_types = 1);

  class Ticket {
    public $id;
    public $name;
    public $type;
    public $status;
    public $info;
    public $priority;
    public $agentId; // Change the type to int
    public $departmentId;
    public $clientId; // Change the type to int

    public function __construct($id, $name, $type, $status, $info, $priority, $agentId, $departmentId, $clientId) {
        $this->id = $id;
        $this->name = $name;
        $this->type = $type;
        $this->status = $status;
        $this->info = $info;
        $this->priority = $priority;
        $this->agentId = (int)$agentId; // Cast to int
        $this->departmentId = $departmentId;
        $this->clientId = (int)$clientId; // Cast to int
    }

    static function getTickets(PDO $db, int $count, string $department = NULL, string $agent = NULL, string $status = NULL, string $priority = NULL, string $hashtag = NULL) : array {
      $where = [];
      $params = [];
  
      if (!is_null($department) && $department !== 'All' && $department !== '') {
        $where[] = 'departmentId = ?';
        $params[] = $department;
      }

      if (!is_null($agent) && $agent !== 'All' && $agent !== '') {
          $where[] = 'agentId = ?';
          $params[] = $agent;
      }

      if (!is_null($status) && $status !== 'All' && $status !== '') {
          $where[] = 'status = ?';
          $params[] = $status;
      }

      if (!is_null($priority) && $priority !== 'All' && $priority !== '') {
          $where[] = 'priority = ?';
          $params[] = $priority;
      }

      if (!is_null($hashtag) && $hashtag !== 'All' && $hashtag !== '') {
          $where[] = 'Hashtag.name LIKE ?';
          $params[] = '%' . $hashtag . '%';
      }

    $whereClause = '';
    if (!empty($where)) {
        $whereClause = 'WHERE ' . implode(' AND ', $where);
    }
  
      // Include JOIN with Hashtag table in your query
      $query = 'SELECT Ticket.ticketId, Ticket.name, Ticket.type, Ticket.status, Ticket.info, Ticket.priority, Ticket.agentId, Ticket.departmentId, Ticket.clientId, GROUP_CONCAT(Hashtag.name) as hashtags
                FROM Ticket 
                LEFT JOIN Hashtag ON Ticket.ticketId = Hashtag.ticketId ' . 
                $whereClause . 
                ' GROUP BY Ticket.ticketId LIMIT ?';
      $params[] = $count;
  
      $stmt = $db->prepare($query);
      $stmt->execute($params);
  
      $tickets = [];
      while ($ticket = $stmt->fetch()) {
        // Check if $ticket['hashtags'] is not null before trying to explode it
        $hashtags = $ticket['hashtags'] !== null ? explode(',', $ticket['hashtags']) : [];
    
        $newTicket = new Ticket(
          $ticket['ticketId'],
          $ticket['name'],
          $ticket['type'],
          $ticket['status'],
          $ticket['info'],
          $ticket['priority'],
          $ticket['agentId'],
          $ticket['departmentId'],
          $ticket['clientId'],
          $hashtags
      );

      $tickets[] = $newTicket;
      }
  
      return $tickets;
  }
  
    
    public static function create(PDO $db, string $name, string $type, string $status, string $info, int $priority = 0, int $agentId = NULL, int $departmentId = NULL, int $clientId): ?Ticket {  
      // If no ticket with the same name is found, insert a new ticket into the database
      $stmt = $db->prepare('INSERT INTO Ticket (name, type, status, info, priority, agentId, departmentId, clientId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)');
      $success = $stmt->execute(array($name, $type, $status, $info, $priority, $agentId, $departmentId, $clientId));
  
      // If the insertion is successful, fetch the newly created ticket and return it as a Ticket object
      if ($success) {
        $ticketId = $db->lastInsertId();
        $ticket = new Ticket(
            $ticketId,
            $name,
            $type,
            $status,
            $info,
            $priority,
            $agentId,
            $departmentId,
            $clientId
          );
          return $ticket;
      }
  
      // If the insertion fails, return null
      return null;
    }

  public static function deleteTicket(PDO $db, int $ticketId): bool {
        $stmt = $db->prepare('DELETE FROM Ticket WHERE ticketId = :ticketId');
        $stmt->bindValue(':ticketId', $ticketId, PDO::PARAM_INT);
        return $stmt->execute();
    }

  static function getTicket(PDO $db, int $ticketId): ?Ticket
  {
    $stmt = $db->prepare('SELECT ticketId, name, type, status, info, priority, agentId, departmentId, clientId FROM Ticket WHERE ticketId = ?');
    $stmt->execute(array($ticketId));
      $ticket = $stmt->fetch();
  
      if ($ticket) {
          return new Ticket(
              intval($ticket['ticketId']),
              $ticket['name'],
              $ticket['type'],
              $ticket['status'],
              $ticket['info'],
              $ticket['priority'],
              $ticket['agentId'],
              $ticket['departmentId'],
              $ticket['clientId']
          );
      }
      return null;
  }
    
    public function save($db)
    {
        $stmt = $db->prepare("UPDATE Ticket SET name = ?, type = ?, info = ?, priority = ? WHERE ticketId = ?");
        
        $stmt->bindValue(1, $this->name);
        $stmt->bindValue(2, $this->type);
        $stmt->bindValue(3, $this->info);
        $stmt->bindValue(4, $this->priority);
        $stmt->bindValue(5, $this->id);
        
        $stmt->execute();
        
        if ($stmt->rowCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

  }
?>