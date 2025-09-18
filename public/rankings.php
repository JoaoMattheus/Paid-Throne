<?php
require __DIR__ . '/../app/bootstrap.php';

use App\Domain\Scales;
use App\Infra\SubmissionRepo;
use App\Support\Formatter;

$repo = new SubmissionRepo();
$scales = Scales::presets();
$selectedScale = $_GET['escala'] ?? '5x2';
if ($selectedScale !== 'global' && !isset($scales[$selectedScale])) {
    $selectedScale = '5x2';
}

$pageTitle = 'Rankings reais - Trono Remunerado';
$pageDescription = 'Top 5 de tempo e dinheiro no trono por escala de trabalho, além do placar global. Compare-se com majestades da mesma rotina.';
$canonical = ($_SERVER['REQUEST_SCHEME'] ?? 'https') . '://' . ($_SERVER['HTTP_HOST'] ?? 'localhost') . '/rankings.php';
include __DIR__ . '/../app/View/partials/head.php';
?>
<section class="card">
    <h1>Rankings coroados por escala</h1>
    <p>Justiça trabalhista exige comparações entre jornadas parecidas. Escolha sua escala abaixo para ver as majestades que reinam mais tempo ou arrecadam mais cacau.</p>
    <div class="rankings-tabs">
        <?php foreach ($scales as $code => $scale): ?>
            <button type="button" data-tab-target="scale-<?= htmlspecialchars($code, ENT_QUOTES) ?>" class="<?= $selectedScale === $code ? 'is-active' : '' ?>"><?= htmlspecialchars($code, ENT_QUOTES) ?> 👑</button>
        <?php endforeach; ?>
        <button type="button" data-tab-target="global" class="<?= $selectedScale === 'global' ? 'is-active' : '' ?>">🌎 Global (todas as escalas)</button>
    </div>
    <?php foreach ($scales as $code => $scale):
        $time = $repo->topTimeByScale($code);
        $money = $repo->topMoneyByScale($code);
        $short = $repo->shortReignsByScale($code);
        ?>
        <section data-tab-panel="scale-<?= htmlspecialchars($code, ENT_QUOTES) ?>" <?= $selectedScale === $code ? '' : 'hidden' ?>>
            <h2><?= htmlspecialchars($scale['label'], ENT_QUOTES) ?></h2>
            <div class="leaderboard-group">
                <h3>👑 Tempo</h3>
                <ol class="leaderboard">
                    <?php if (empty($time)): ?>
                        <li>Nenhum reinado registrado ainda.</li>
                    <?php else: ?>
                        <?php foreach ($time as $row): ?>
                            <li><span><?= htmlspecialchars($row['nickname'], ENT_QUOTES) ?></span><strong><?= Formatter::minutesToHuman((int) $row['throne_minutes_month']) ?></strong></li>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </ol>
            </div>
            <div class="leaderboard-group">
                <h3>💰 Fortunas</h3>
                <ol class="leaderboard">
                    <?php if (empty($money)): ?>
                        <li>Nenhum reinado registrado ainda.</li>
                    <?php else: ?>
                        <?php foreach ($money as $row): ?>
                            <li><span><?= htmlspecialchars($row['nickname'], ENT_QUOTES) ?></span><strong><?= Formatter::money((float) $row['throne_money_month']) ?></strong></li>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </ol>
            </div>
            <div class="leaderboard-group">
                <h3>🥀 Reinos curtinhos</h3>
                <ol class="leaderboard">
                    <?php if (empty($short)): ?>
                        <li>Nenhum reinado registrado ainda.</li>
                    <?php else: ?>
                        <?php foreach ($short as $row): ?>
                            <li><span><?= htmlspecialchars($row['nickname'], ENT_QUOTES) ?></span><strong><?= Formatter::minutesToHuman((int) $row['throne_minutes_month']) ?></strong></li>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </ol>
            </div>
        </section>
    <?php endforeach; ?>
    <?php
    $globalTime = $repo->topTimeGlobal();
    $globalMoney = $repo->topMoneyGlobal();
    $globalShort = $repo->shortReignsGlobal();
    ?>
    <section data-tab-panel="global" <?= $selectedScale === 'global' ? '' : 'hidden' ?> id="global">
        <h2>🌎 Global (todas as escalas)</h2>
        <p class="form-hint">Comparação entre escalas pode ser injusta, use com senso de humor e responsabilidade.</p>
        <div class="leaderboard-group">
            <h3>👑 Tempo</h3>
            <ol class="leaderboard">
                <?php if (empty($globalTime)): ?>
                    <li>Nenhum reinado registrado ainda.</li>
                <?php else: ?>
                    <?php foreach ($globalTime as $row): ?>
                        <li>
                            <span><?= htmlspecialchars($row['nickname'], ENT_QUOTES) ?></span>
                            <span>
                                <?= Formatter::minutesToHuman((int) $row['throne_minutes_month']) ?>
                                <span class="badge-scale"><?= htmlspecialchars($row['scale_code'], ENT_QUOTES) ?></span>
                            </span>
                        </li>
                    <?php endforeach; ?>
                <?php endif; ?>
            </ol>
        </div>
        <div class="leaderboard-group">
            <h3>💰 Fortunas</h3>
            <ol class="leaderboard">
                <?php if (empty($globalMoney)): ?>
                    <li>Nenhum reinado registrado ainda.</li>
                <?php else: ?>
                    <?php foreach ($globalMoney as $row): ?>
                        <li>
                            <span><?= htmlspecialchars($row['nickname'], ENT_QUOTES) ?></span>
                            <span>
                                <?= Formatter::money((float) $row['throne_money_month']) ?>
                                <span class="badge-scale"><?= htmlspecialchars($row['scale_code'], ENT_QUOTES) ?></span>
                            </span>
                        </li>
                    <?php endforeach; ?>
                <?php endif; ?>
            </ol>
        </div>
        <div class="leaderboard-group">
            <h3>🥀 Reinos curtinhos</h3>
            <ol class="leaderboard">
                <?php if (empty($globalShort)): ?>
                    <li>Nenhum reinado registrado ainda.</li>
                <?php else: ?>
                    <?php foreach ($globalShort as $row): ?>
                        <li>
                            <span><?= htmlspecialchars($row['nickname'], ENT_QUOTES) ?></span>
                            <span>
                                <?= Formatter::minutesToHuman((int) $row['throne_minutes_month']) ?>
                                <span class="badge-scale"><?= htmlspecialchars($row['scale_code'], ENT_QUOTES) ?></span>
                            </span>
                        </li>
                    <?php endforeach; ?>
                <?php endif; ?>
            </ol>
        </div>
    </section>
</section>
<?php include __DIR__ . '/../app/View/partials/footer.php';
