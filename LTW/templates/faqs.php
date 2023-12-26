<?php
declare(strict_types=1);

function drawFaqs($faqs)
{
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FAQ PAGE</title>
    <link href="https://fonts.googleapis.com/css2?family=Work+Sans&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../styles/faqs.css">
    <link rel="stylesheet" href="../styles/global.css">
</head>
<body>
    <main>
        <h1 class="faq-heading">Frequently Asked Questions</h1>
        <section class="faq-container">
            <?php foreach ($faqs as $faq): ?>
            <div class="faq-item">
                <!-- faq question -->
                <h1 class="faq-page"><?php echo $faq->question; ?></h1>
                <!-- faq answer -->
                <div class="faq-body">
                    <p><?php echo $faq->answer; ?></p>
                </div>
            </div>
            <hr class="hr-line">
            <?php endforeach; ?>
        </section>
    </main>
    <script src="../javascript/faqs.js"></script>
</body>
</html>
<?php
}
?>
