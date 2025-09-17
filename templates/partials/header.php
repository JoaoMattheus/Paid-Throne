<?php
require_once BASE_PATH . '/bootstrap.php';

$siteName = (string) env('APP_NAME', 'Trono Remunerado');
$baseUrl = rtrim((string) env('APP_URL', ''), '/');
$currentUri = $_SERVER['REQUEST_URI'] ?? '/';
$canonical = $canonical ?? ($baseUrl !== '' ? $baseUrl . $currentUri : $currentUri);
$metaDescription = $metaDescription ?? 'Calcule quanto você ganha enquanto está no banheiro com a calculadora oficial do Trono Remunerado.';
$pageTitle = $pageTitle ?? $siteName;
$metaTitle = $metaTitle ?? $pageTitle;
$metaImage = $metaImage ?? ($baseUrl !== '' ? $baseUrl . '/assets/images/og-banner.svg' : '/assets/images/og-banner.svg');
$adsenseClient = env('ADSENSE_CLIENT_ID');
$analyticsId = env('ANALYTICS_ID');
?>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><?= sanitize_output($metaTitle); ?></title>
    <meta name="description" content="<?= sanitize_output($metaDescription); ?>">
    <link rel="canonical" href="<?= sanitize_output($canonical); ?>">
    <meta name="author" content="<?= sanitize_output($siteName); ?>">
    <meta name="robots" content="index,follow">
    <meta property="og:type" content="website">
    <meta property="og:title" content="<?= sanitize_output($metaTitle); ?>">
    <meta property="og:description" content="<?= sanitize_output($metaDescription); ?>">
    <meta property="og:url" content="<?= sanitize_output($canonical); ?>">
    <meta property="og:image" content="<?= sanitize_output($metaImage); ?>">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="<?= sanitize_output($metaTitle); ?>">
    <meta name="twitter:description" content="<?= sanitize_output($metaDescription); ?>">
    <meta name="twitter:image" content="<?= sanitize_output($metaImage); ?>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap">
    <link rel="stylesheet" href="<?= asset('assets/css/style.css'); ?>">
    <link rel="icon" href="<?= asset('assets/images/favicon.svg'); ?>" type="image/svg+xml">
    <?php if ($adsenseClient): ?>
        <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=<?= sanitize_output($adsenseClient); ?>" crossorigin="anonymous"></script>
    <?php endif; ?>
    <?php if ($analyticsId): ?>
        <script async src="https://www.googletagmanager.com/gtag/js?id=<?= sanitize_output($analyticsId); ?>"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);} // eslint-disable-line
            gtag('js', new Date());
            gtag('config', '<?= sanitize_output($analyticsId); ?>');
        </script>
    <?php endif; ?>
    <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "WebSite",
            "name": "<?= sanitize_output($siteName); ?>",
            "url": "<?= sanitize_output($baseUrl !== '' ? $baseUrl : $canonical); ?>",
            "description": "<?= sanitize_output($metaDescription); ?>",
            "potentialAction": {
                "@type": "SearchAction",
                "target": "<?= sanitize_output($baseUrl !== '' ? $baseUrl : ''); ?>/?s={search_term_string}",
                "query-input": "required name=search_term_string"
            }
        }
    </script>
</head>
<body>
<header class="site-header">
    <div class="container header__content">
        <a class="brand" href="/">
            <span class="brand__icon" aria-hidden="true">🚽</span>
            <span class="brand__text"><?= sanitize_output($siteName); ?></span>
        </a>
        <button class="header__toggle" aria-expanded="false" aria-controls="primary-navigation">
            <span class="sr-only">Abrir menu</span>
            ☰
        </button>
        <nav id="primary-navigation" class="header__nav">
            <ul>
                <li><a href="/" class="<?= ($currentUri === '/' ? 'is-active' : ''); ?>">Calculadora</a></li>
                <li><a href="/contato.php" class="<?= (str_contains($currentUri, 'contato') ? 'is-active' : ''); ?>">Contato</a></li>
                <li><a href="/privacidade.php" class="<?= (str_contains($currentUri, 'privacidade') ? 'is-active' : ''); ?>">Privacidade</a></li>
            </ul>
        </nav>
    </div>
</header>
<main class="page-content">
