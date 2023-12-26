<?php function drawEditTicket(Session $session, $db, $ticket, $departments) {; ?>
  <div class="edit-profile-container">
    <h3>Edit Ticket</h3>
    <form action="../actions/action_editTicket.php" method="post" class="edit-profile-form">
        
        <input type="hidden" name="ticketId" value="<?php echo $ticket->id; ?>">
      
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="<?php echo $ticket->name; ?>">

        <label for="type">Type:</label>
        <select id="type" name="type">
          <?php foreach ($departments as $department) { ?>
            <option value="<?php echo $department->name; ?>" <?php if ($ticket->type === $department->name) echo "selected"; ?>><?php echo $department->name; ?></option>
          <?php } ?>
        </select>
      
        <label for="info">Info:</label>
        <input type="text" id="info" name="info" value="<?php echo $ticket->info; ?>">

        <label for="priority">Priority:</label>
        <select id="priority" name="priority">
          <?php for ($i = 1; $i <= 4; $i++) { ?>
            <option value="<?php echo $i; ?>" <?php if ($ticket->priority == $i) echo "selected"; ?>><?php echo $i; ?></option>
          <?php } ?>
        </select>

        <input type="submit" value="Update Ticket" class="button">
    </form>
  </div>
  <link rel="stylesheet" href="../styles/edit_user.css">
  <link rel="stylesheet" href="../styles/global.css">
<?php } ?>
