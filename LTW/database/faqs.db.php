<?php
  declare(strict_types = 1);

  class Faq {
    public int $faqId;
    public string $question;
    public string $answer;


    public function __construct(int $faqId, string $question, string $answer){
      $this->faqId = $faqId;
      $this->question = $question;
      $this->answer = $answer;
    }

    static function getFaqs(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT faqId, question, answer FROM Faqs LIMIT ?');
      $stmt->execute(array($count));

      $faqs = array();
      while($faq = $stmt->fetch()) {
        $faqs[] = new Faq(
         $faq['faqId'],
         $faq['question'],
         $faq['answer'],
        );
      }

      return $faqs;
    }
  }
?>