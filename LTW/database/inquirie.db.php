<?php
  declare(strict_types = 1);

  class Inquirie {
    public int $id;
    public string $question;
    public string $answer;
    public int $agentId;
    public int $clientId;

    public function __construct(int $id, string $question, string $answer, int $agentId, int $clientId){
      $this->id = $id;
      $this->question = $question;
      $this->answer = $answer;
      $this->agentId = $agentId;
      $this->agentId = $agentId;
    }

    static function getInquiries(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT * inquirieId, question, answer, agentId, clientId FROM Inquirie LIMIT ?');
      $stmt->execute(array($count));

      $inquiries = array();
      while($inquirie = $stmt->fetch()) {
        $inquiries[] = new Inquirie(
         $inquirie['inquirieId'],
         $inquirie['question'],
         $inquirie['answer'],
         $inquirie['agentId'],
         $inquirie['clientId'],
        );
      }

      return $inquiries;
    }
  }
?>