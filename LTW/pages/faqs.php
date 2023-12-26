<?php
    declare(strict_types = 1);

    require_once(__DIR__ . '/../utils/session.php');
    $session = new Session();

    require_once(__DIR__ . '/../database/connection.db.php');
    require_once(__DIR__ . '/../database/faqs.db.php');
    require_once(__DIR__ . '/../templates/footer.php');
    require_once(__DIR__ . '/../templates/header.php');

    require_once(__DIR__ . '/../templates/faqs.php');   

    $db = getDatabaseConnection();
    $faqs = Faq::getFaqs($db, 10);
    $type = (User::getUser($db, $session->getId()))->type;

    drawHeader($session, $type);
    drawFaqs($faqs);
    drawFooter();
?>