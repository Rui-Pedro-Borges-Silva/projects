<?php
  declare(strict_types = 1);

  class Department {
    public int $id;
    public string $name;
    public int $availableTeams;

    public function __construct(int $id, string $name, int $availableTeams){
      $this->id = $id;
      $this->name = $name;
      $this->availableTeams = $availableTeams;
    }

    static function getDepartments(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT departmentId, name, availableTeams FROM Department LIMIT ?');
      $stmt->execute(array($count));

      $departments = array();
      while($department = $stmt->fetch()) {
        $departments[] = new Department(
         $department['departmentId'],
         $department['name'],
         $department['availableTeams'],
        );
      }

      return $departments;
    }
  }
?>