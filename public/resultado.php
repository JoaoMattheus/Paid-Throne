<?php
require __DIR__ . '/../app/bootstrap.php';

use App\Infra\SubmissionRepo;
use App\Support\Formatter;

session_start();

$id = (int) ($_GET['id'] ?? 0);
if ($id <= 0) {
    header('Location: /');
    exit;
}

$repo = new SubmissionRepo();
$submission = $repo->find($id);

if ($submission === null) {
    header('Location: /');
    exit;
}

$minutesMonth = (int) $submission['throne_minutes_month'];
$minutesYear = (int) $submission['throne_minutes_year'];
$moneyMonth = (float) $submission['throne_money_month'];
$moneyYear = (float) $submission['throne_money_year'];
$scaleCode = (string) $submission['scale_code'];
$hoursDay = (float) $submission['hours_per_day'];
$progress = min(100, max(0, ((int) $submission['minutes_per_day'] / 120) * 100));

$hourValue = null;
if (!empty($_SESSION['last_result']) && (int) $_SESSION['last_result']['id'] === $id) {
    $hourValue = (float) $_SESSION['last_result']['hour_value'];
    $throneHoursMonth = (float) $_SESSION['last_result']['throne_hours_month'];
    unset($_SESSION['last_result']);
} else {
    $throneHoursMonth = $minutesMonth / 60.0;
    $hourValue = $throneHoursMonth > 0 ? $moneyMonth / $throneHoursMonth : 0.0;
}

$pageTitle = 'Resultado do seu reinado - Trono Remunerado';
$pageDescription = 'Veja quanto tempo e quanto dinheiro você conquistou no trono nesta escala.';
$canonical = ($_SERVER['REQUEST_SCHEME'] ?? 'https') . '://' . ($_SERVER['HTTP_HOST'] ?? 'localhost') . '/resultado.php?id=' . $id;
include __DIR__ . '/../app/View/partials/head.php';
?>
<section class="card result-card">
    <h1>Majestade, eis o seu relatório real!</h1>
    <p class="result-highlight">🏰 Sua Majestade reinou <?= Formatter::minutesToHuman($minutesMonth) ?> neste mês (≈ <?= $minutesMonth ?> min).</p>
    <p>Esse trono rendeu <strong><?= Formatter::money($moneyMonth) ?></strong>. Em 12 meses: <strong><?= Formatter::minutesToHuman($minutesYear) ?></strong> e <strong><?= Formatter::money($moneyYear) ?></strong>. Longa vida ao <strong>cag$flow real</strong>!</p>
    <div class="progress" aria-hidden="true">
        <span style="width: <?= htmlspecialchars(number_format($progress, 2, '.', ''), ENT_QUOTES) ?>%"></span>
    </div>
    <ul>
        <li>Escala: <strong><?= htmlspecialchars($scaleCode, ENT_QUOTES) ?></strong> com <?= number_format($hoursDay, 2, ',', '.') ?>h/dia.</li>
        <li>Valor-hora estimado: <strong><?= Formatter::money($hourValue) ?></strong>.</li>
        <li>Dias trabalhados no mês da escala: <strong><?= number_format((float) $submission['days_per_month'], 2, ',', '.') ?></strong>.</li>
    </ul>
    <div class="cta-row">
        <a class="button-primary" href="/rankings.php?escala=<?= urlencode($scaleCode) ?>">Ver rankings da minha escala</a>
        <button type="button" class="button-primary" data-share data-share-text="<?= htmlspecialchars('Eu reino ' . Formatter::minutesToHuman($minutesMonth) . ' no Trono Remunerado!', ENT_QUOTES) ?>">Compartilhar</button>
    </div>
</section>
<section class="card">
    <h2>E agora?</h2>
    <p>Compartilhe com a galera do trabalho, compare com colegas de escala e veja se o seu reinado merece um reajuste. Só não esqueça: estimativa lúdica com base na CLT — orientação jurídica é com profissionais.</p>
    <p><a href="/">Calcular de novo</a> ou <a href="/rankings.php#global">espiar os rankings gerais</a>.</p>
</section>
<?php include __DIR__ . '/../app/View/partials/footer.php';
