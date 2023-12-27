<?php
  declare(strict_types = 1);

  class User {
    public int $id;
    public string $username;
    public string $email;
    public string $name;
    public string $type;
    public string $image;


    public function __construct(int $id, string $username, string $email, string $name, string $type, string $image){
      $this->id = $id;
      $this->username = $username;
      $this->email = $email;
      $this->name = $name;
      $this->type = $type;
      $this->image = $image;
    }

    static function getUsers(PDO $db, int $count) :  array {
      $stmt = $db->prepare('SELECT * FROM User LIMIT ?');
      $stmt->execute(array($count));

      $users = array();
      while($user = $stmt->fetch()) {
        $users[] = new User(
         (int)$user['userId'],
         $user['username'],
         $user['email'],
         $user['name'],
         $user['type'],
         $user['image']
        );
      }

      return $users;
    }

    static function getAgents(PDO $db, int $count) : array {
      $stmt = $db->prepare('SELECT * FROM User WHERE type = "agent" LIMIT ?');
      $stmt->execute(array($count));

      $agents = array();
      while($agent = $stmt->fetch()) {
        $agents[] = new User(
         (int)$agent['userId'],
         $agent['username'],
         $agent['email'],
         $agent['name'],
         $agent['type'],
         $agent['image']
        );
      }

      return $agents;
    }

    static function getClients(PDO $db, int $count) : array {
      $stmt = $db->prepare('SELECT * FROM User WHERE type = "client" LIMIT ?');
      $stmt->execute(array($count));

      $clients = array();
      while($client = $stmt->fetch()) {
         $clients[] = new User(
         (int)$client['userId'],
         $client['username'],
         $client['email'],
         $client['name'],
         $client['type'],
         $client['image']
        );
      }

      return $clients;
    }

    static function login(PDO $db, string $username, string $password) : ?User {
      $stmt = $db->prepare('SELECT * FROM User WHERE username = ?');
      $stmt->execute(array($username));
      $user = $stmt->fetch();
  
      if ($user && password_verify($password, $user['password'])) {
        return new User(
              (int)$user['userId'],
              $user['username'],
              $user['email'],
              $user['name'],
              $user['type'],
              $user['image']
          );
      }
  
      return null;
    }
    
    static function create(PDO $db, string $username, string $hashed_password, string $email, string $name, string $type, string $image) : ?User {
      // First, check if the user with the same username or email already exists
      $stmt = $db->prepare('SELECT * FROM User WHERE username = ? OR email = ?');
      $stmt->execute(array($username, $email));
      $user = $stmt->fetch();
  
      // If a user with the same username or email is found, return null
      if ($user) {
          return null;
      }
  
      // If no user with the same username or email is found, insert a new user into the database
      $stmt = $db->prepare('INSERT INTO User (username, password, email, name, type, image) VALUES (?, ?, ?, ?, ?, ?)');
      $success = $stmt->execute(array($username, $hashed_password, $email, $name, $type, $image));
  
      // If the insertion is successful, fetch the newly created user and return it as a User object
      if ($success) {
          $userId = $db->lastInsertId();
          return new User((int) $userId, $username, $hashed_password, $email, $name, $type, $image);
      }
  
      // If the insertion fails, return null
      return null;
  }

  static function getUser(PDO $db, int $userId): ?User
  {
    $stmt = $db->prepare('SELECT userId, username, email, name, type, image FROM User WHERE userId = ?');
    $stmt->execute(array($userId));
      $user = $stmt->fetch();
  
      if ($user) {
          return new User(
              intval($user['userId']),
              $user['username'],
              $user['email'],
              $user['name'],
              $user['type'],
              $user['image']
          );
      }
      return null;
  }
  
  public static function deleteUser(PDO $db, int $userId): bool {
        $stmt = $db->prepare('DELETE FROM User WHERE userId = :userId');
        $stmt->bindValue(':userId', $userId, PDO::PARAM_INT);
        return $stmt->execute();
    }

    static function updateUser(PDO $db, User $user): bool
    {
        $stmt = $db->prepare('UPDATE User SET username = ?, email = ?, name = ? WHERE userId = ?');
        $success = $stmt->execute(array($user->username, $user->email, $user->name, $user->id));

        return $success;
    }

    public function save($db, $newPassword = null) {
      if ($newPassword !== null) {
          // If a new password is provided, update the password in the database
          $hashedPassword = password_hash($newPassword, PASSWORD_DEFAULT);
          $query = "UPDATE User SET name = :name, username = :username, email = :email, password = :password WHERE userId = :id";
          $stmt = $db->prepare($query);
          $stmt->bindParam(':password', $hashedPassword);
      } else {
          // If no new password is provided, update the user's information without changing the password
          $query = "UPDATE User SET name = :name, username = :username, email = :email WHERE userId = :id";
          $stmt = $db->prepare($query);
      }
      
      $stmt->bindParam(':name', $this->name);
      $stmt->bindParam(':username', $this->username);
      $stmt->bindParam(':email', $this->email);
      $stmt->bindParam(':id', $this->id);
      $stmt->execute();
    }

    public function edit($db){
      $query = "UPDATE User SET username = :username, email = :email, type = :type WHERE userId = :id";
      $stmt = $db->prepare($query);

      $stmt->bindParam(':username', $this->username);
      $stmt->bindParam(':email', $this->email);
      $stmt->bindParam(':type', $this->type);
      $stmt->bindParam(':id', $this->id);
      $stmt->execute();
    }
  }
?>