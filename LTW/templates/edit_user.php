<?php function drawEditUser(Session $session, $db, $user) {; ?>
  <div class="edit-profile-container">
    <h3>Edit User</h3>
    <form action="../actions/action_editUser.php" method="post" class="edit-profile-form">
        
        <input type="hidden" name="userId" value="<?php echo $user->id; ?>">
      
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="<?php echo $user->username; ?>">
      
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="<?php echo $user->email; ?>">

        <label for="role">Role:</label>
        <select id="role" name="role">
          <option value="client" <?php if ($user->type === "client") echo "selected"; ?>>Client</option>
          <option value="agent" <?php if ($user->type === "agent") echo "selected"; ?>>Agent</option>
          <option value="admin" <?php if ($user->type === "admin") echo "selected"; ?>>Admin</option>
        </select>

        <input type="submit" value="Update User" class="button">
    </form>
  </div>
  <link rel="stylesheet" href="../styles/edit_user.css">
  <link rel="stylesheet" href="../styles/global.css">
<?php } ?>
