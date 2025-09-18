<?php
/** @var string $pageTitle */
/** @var string $pageDescription */
/** @var string $canonical */
$pageTitle = $pageTitle ?? 'Trono Remunerado';
$pageDescription = $pageDescription ?? 'Descubra quanto rende seu tempo real no banheiro.';
$canonical = $canonical ?? '';
?>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><?= htmlspecialchars($pageTitle, ENT_QUOTES) ?></title>
    <meta name="description" content="<?= htmlspecialchars($pageDescription, ENT_QUOTES) ?>">
    <?php if ($canonical): ?>
        <link rel="canonical" href="<?= htmlspecialchars($canonical, ENT_QUOTES) ?>">
    <?php endif; ?>
    <meta property="og:type" content="website">
    <meta property="og:title" content="<?= htmlspecialchars($pageTitle, ENT_QUOTES) ?>">
    <meta property="og:description" content="<?= htmlspecialchars($pageDescription, ENT_QUOTES) ?>">
    <meta property="og:url" content="<?= htmlspecialchars($canonical ?: ($_SERVER['REQUEST_SCHEME'] ?? 'https') . '://' . ($_SERVER['HTTP_HOST'] ?? 'localhost') . $_SERVER['REQUEST_URI'], ENT_QUOTES) ?>">
    <meta property="og:image" content="/assets/social/trono-og.webp">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="<?= htmlspecialchars($pageTitle, ENT_QUOTES) ?>">
    <meta name="twitter:description" content="<?= htmlspecialchars($pageDescription, ENT_QUOTES) ?>">
    <link rel="preload" href="/assets/fonts/trono-display.woff2" as="font" type="font/woff2" crossorigin>
    <link rel="stylesheet" href="/assets/css/app.css">
    <script defer src="/assets/js/app.js"></script>
</head>
<body>
<header class="site-header">
    <div class="container">
        <a href="/" class="logo" aria-label="Trono Remunerado">
            <span aria-hidden="true">💩</span>
            <span class="logo-text">Trono Remunerado</span>
        </a>
        <nav class="site-nav">
            <a href="/rankings.php">Rankings</a>
        </nav>
    </div>
</header>
<main class="site-main container">
