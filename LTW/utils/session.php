
<?php 
    require_once(__DIR__ . '/../database/user.db.php');

    class Session {
        private $messages;


        public function clearMessages() {
            $this->messages = array();
            $_SESSION['messages'] = array();
        }
        
        public function __construct() {
            session_start();
      
            $this->messages = isset($_SESSION['messages']) ? $_SESSION['messages'] : array();
            unset($_SESSION['messages']);
        }
        
        public function login(User $user) {
            $_SESSION['id'] = $user->id;
            $_SESSION['username'] = $user->username;
            // and so on for other user properties you want to store
        }

        public function isLoggedIn() : bool {
            return isset($_SESSION['id']);    
        }
      
        public function logout() {
            unset($_SESSION['id']);
        }

        public function getId() : ?int {
            return isset($_SESSION['id']) ? $_SESSION['id'] : null;    
        }

        public function getUsername() : ?string {
            return isset($_SESSION['username']) ? $_SESSION['username'] : null;    
        }

        public function getEmail() : ?string {
            return isset($_SESSION['email']) ? $_SESSION['email'] : null;    
        }

        public function getName() : ?string {
            return isset($_SESSION['name']) ? $_SESSION['name'] : null;
        }

        public function getType() : ?string {
            return isset($_SESSION['type']) ? $_SESSION['type'] : null;
        }
        
        public function getMessages() : array {
            return $this->messages;
        }
        
        public function addMessage(string $type, string $text) {
            $_SESSION['messages'][] = array('type' => $type, 'text' => $text);
          }

    }
?>