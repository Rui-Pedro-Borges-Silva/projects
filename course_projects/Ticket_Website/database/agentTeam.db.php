<?php
  declare(strict_types = 1);

  class AgentTeam {
    public int $agentId;
    public int $teamId;

    public function __construct(int $agentId, int $teamId){
      $this->agentId = $agentId;
      $this->teamId = $teamId;
    }

    static function getAgentsTeams(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT * agentId, teamId FROM AgentTeam LIMIT ?');
      $stmt->execute(array($count));
      $agentsTeams = array();
      while($agentsTeam = $stmt->fetch()) {
        $agentsTeams[] = new AgentTeam(
         $agentsTeam['agentId'],
         $agentsTeam['teamId'],
        );
      }

      return $agentsTeams;
    }
  }
?>