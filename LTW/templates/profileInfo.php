<?php
    declare(strict_types = 1);

    require_once(__DIR__ . '/../utils/session.php');
?>

                    <!-- EDIT PROFILE -->
<?php function drawEditForm(Session $session, $db) { 
  $user = User::getUser($db, $session->getId())?>\
  <?php $avatarUrl = $user->image ? $user->image : 'default_avatar.jpg'; ?>
  <div class="edit-profile-container">
    <form action="../actions/action_editProfile.php" method="post" class="edit-profile-form">
        <label for="avatar">Profile Avatar:</label>
        <div class="profile-avatar-wrapper">
          <img src="<?php echo $avatarUrl; ?>" alt="Profile Avatar" class="profile-avatar">
        </div>
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="<?php echo $user->name; ?>">

        <label for="email">Username:</label>
        <input type="text" id="username" name="username" value="<?php echo $user->username; ?>">
      
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="<?php echo $user->email; ?>">

        <label for="current_password">Current Password:</label>
        <input type="password" id="current_password" name="current_password">

        <label for="new_password">New Password:</label>
        <input type="password" id="new_password" name="new_password">

        <label for="confirm_password">Confirm Password:</label>
        <input type="password" id="confirm_password" name="confirm_password">

        <input type="submit" value="Update Profile" class="button">
    </form>
  </div>
  <link rel="stylesheet" href="../styles/edit_profile.css">
  <link rel="stylesheet" href="../styles/global.css">
<?php } ?>


<?php
function drawInfo(Session $session, $db)
{
    $user = User::getUser($db, $session->getId());

    if ($user !== null) {
        $avatarUrl = $user->image ? $user->image : 'default_avatar.jpg'; // Set a default avatar image path here

        ?>
        <div class="profile-container">
            <div class="profile-card">
                <div class="profile-avatar-wrapper">
                    <img src="<?php echo $avatarUrl; ?>" alt="Profile Avatar" class="profile-avatar">
                </div>
                <div class="profile-info">
                    <p><strong>Welcome, <?php echo $user->name; ?></strong></p>
                    <p><strong>Username: <?php echo $user->username; ?></strong></p>
                    <p><strong>Email: <?php echo $user->email; ?></strong></p>
                    <p><strong>Role: <?php echo $user->type; ?></strong></p>
                    <!-- feature to add -->
                    <!--<p><strong>Member Since:</strong> January 1, 2023</p>-->
                </div>
            </div>
            <a href="edit_profile.php" class="button">Edit Profile</a>
        </div>
        <link rel="stylesheet" href="../styles/global.css">
        <link rel="stylesheet" href="../styles/profilee.css">
        <?php
    } else {
        // User not found, display an appropriate message or take necessary action
    }
}
?>





