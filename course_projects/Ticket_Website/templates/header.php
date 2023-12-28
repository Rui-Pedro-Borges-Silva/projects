<?php 
  declare(strict_types = 1); 

  require_once(__DIR__ . '/../utils/session.php');
?>

<?php function drawHeader($session ,$type){ ?>
<!DOCTYPE html> 
<html lang="en-US">
  <head>
    <title>Problem Solvers</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet"> <!-- Add this line -->
    <link rel="stylesheet" href="../styles/navbar.css">
    <link rel="stylesheet" href="../styles/global.css">
  </head>
  <body>
    <section id="messages">
        <?php
        foreach ($session->getMessages() as $messsage) { ?>
        <article class="<?=$messsage['type']?>">
          <?=$messsage['text']?>
        </article>
        <?php }
        ?> 
    </section>

    <header>
      <h1>
        <a href='/../pages/index.php'>Problem Solvers</a>
      </h1>
      <div class="nav-links">
        <a href='/../pages/profile.php'>Profile</a>
        <a href='/../pages/faqs.php'>FAQs</a>
        <?php if($type === 'admin'):?>
          <a href='/../pages/tickets.php'>Open Tickets</a>
          <a href='/../pages/users.php'>Manage Users</a>
        <?php endif;?>
        <?php if($type === 'client' || $type === 'agent'):?>
          <a href='/../pages/tickets.php'>My Tickets</a>
        <?php endif;?>
      </div>
    </header>
    </body>

  </html>
<?php } ?>

<?php function drawInitialHeader($session){ ?>
<!DOCTYPE html> 
<html lang="en-US">
  <head>
    <title>Problem Solvers</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet"> <!-- Add this line -->
    <link rel="stylesheet" href="../styles/navbar.css">
    <link rel="stylesheet" href="../styles/global.css">
  </head>
  <body>
    <section id="messages">
        <?php
        foreach ($session->getMessages() as $messsage) { ?>
        <article class="<?=$messsage['type']?>">
          <?=$messsage['text']?>
        </article>
        <?php }
        ?> 
    </section>
    <header>
      <h1>
        <a href='/../pages/index.php'>Problem Solvers</a>
      </h1>
      <div class="nav-links">
      </div>
    </header>
    </body>

  </html>
<?php } ?>



