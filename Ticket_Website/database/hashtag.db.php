<?php
  declare(strict_types = 1);

  class Hashtag {
    public int $id;
    public string $name;
    public int $ticketId;

    public function __construct(int $id, string $name, int $ticketId){
      $this->id = $id;
      $this->name = $name;
      $this->ticketId = $ticketId;
    }

    static function getHashtags(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT * hashtagId, name, ticketId FROM Hashtag LIMIT ?');
      $stmt->execute(array($count));

      $hashtags = array();
      while($hashtag = $stmt->fetch()) {
        $hashtags[] = new Hashtag(
         $hashtag['hashtagId'],
         $hashtag['name'],
         $hashtag['ticketId'],
        );
      }

      return $hashtags;
    }
  }
?>