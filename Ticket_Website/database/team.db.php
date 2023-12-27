<?php
  declare(strict_types = 1);

  class Team {
    public int $id;
    public string $name;
    public int $capacity;
    public int $numberOfTickets;
    public int $departmentId;

    public function __construct(int $id, string $name, int $capacity, int $numberOfTickets, int $departmentId){
      $this->id = $id;
      $this->name = $name;
      $this->capacity = $capacity;
      $this->numberOfTickets = $numberOfTickets;
      $this->departmentId = $departmentId;
    }

    static function getTeams(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT * teamId, name, capacity, numberOfTickets, departmentId FROM Team LIMIT ?');
      $stmt->execute(array($count));

      $teams = array();
      while($team = $stmt->fetch()) {
        $teams[] = new Team(
         $team['teamId'],
         $team['name'],
         $team['capacity'],
         $team['numberOfTickets'],
         $team['departmentId'],
        );
      }

      return $teams;
    }
  }
?>