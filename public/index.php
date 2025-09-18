<?php
require __DIR__ . '/../app/bootstrap.php';

use App\Domain\Scales;

session_start();
$formError = $_SESSION['form_error'] ?? null;
$old = $_SESSION['form_old'] ?? [];
unset($_SESSION['form_error'], $_SESSION['form_old']);

$pageTitle = 'Trono Remunerado - calcule quanto rende seu tempo no banheiro (por escala)';
$pageDescription = 'Descubra quantos minutos por mês você reina no trono e quanto isso rende em dinheiro. Suporta 5x2, 6x1, 12x36 e 4x2. Rankings por escala e zero coleta de salário.';
$canonical = ($_SERVER['REQUEST_SCHEME'] ?? 'https') . '://' . ($_SERVER['HTTP_HOST'] ?? 'localhost') . '/';
$scales = Scales::presets();
include __DIR__ . '/../app/View/partials/head.php';
?>
<section class="hero">
    <h1>Calcule o valor do seu reinado no trono 💩</h1>
    <p>Informativo bem-humorado que transforma o tempo de banheiro em horas trabalhadas e reais. Rankings por escala, zero coleta de salário. É diversão com cheiro de justiça laboral.</p>
</section>
<section class="card">
    <?php if ($formError): ?>
        <div class="alert" role="alert"><?= htmlspecialchars($formError, ENT_QUOTES) ?></div>
    <?php endif; ?>
    <form action="/calcular.php" method="post" class="form-grid" data-throne-form>
        <div class="form-group full">
            <label for="apelido">Apelido real</label>
            <input type="text" id="apelido" name="apelido" maxlength="40" required placeholder="Rainha do Flush, Rei da Faxina..." value="<?= htmlspecialchars((string) ($old['apelido'] ?? ''), ENT_QUOTES) ?>">
        </div>
        <div class="form-group">
            <label for="salario">Salário líquido mensal (R$)</label>
            <input type="text" id="salario" name="salario" required inputmode="decimal" placeholder="Ex.: 2500,00" aria-describedby="tooltip-salario" value="<?= htmlspecialchars((string) ($old['salario'] ?? ''), ENT_QUOTES) ?>">
            <small class="form-hint" id="tooltip-salario">Usamos só para calcular o valor da hora. <strong>Nunca salvamos</strong> seu salário. Promessa de rei.</small>
        </div>
        <div class="form-group">
            <label for="minutos">Tempo médio no trono por dia útil (min)</label>
            <input type="number" id="minutos" name="minutos" required min="1" max="120" value="<?= htmlspecialchars((string) ($old['minutos'] ?? '10'), ENT_QUOTES) ?>">
        </div>
        <div class="form-group">
            <label for="escala">Escala de trabalho</label>
            <select id="escala" name="escala" required data-scale-select>
                <?php foreach ($scales as $code => $scale): ?>
                    <option value="<?= htmlspecialchars($code, ENT_QUOTES) ?>" data-default-hours="<?= htmlspecialchars((string) $scale['default_hours_per_day'], ENT_QUOTES) ?>" <?= (($old['escala'] ?? '5x2') === $code) ? 'selected' : '' ?>>
                        <?= htmlspecialchars($scale['label'], ENT_QUOTES) ?>
                    </option>
                <?php endforeach; ?>
            </select>
            <small class="form-hint">Limite padrão CLT: 8h/dia e 44h/semana. Ajuste se sua jornada for diferente.</small>
        </div>
        <div class="form-group">
            <label for="horas">Horas trabalhadas por dia</label>
            <?php
            $defaultHours = $scales['5x2']['default_hours_per_day'];
            $hoursValue = $old['horas'] ?? $defaultHours;
            ?>
            <input type="number" id="horas" name="horas" step="0.01" min="1" max="24" value="<?= htmlspecialchars((string) $hoursValue, ENT_QUOTES) ?>" data-hours-input <?= ($old['horas'] ?? '') !== '' ? 'data-user-edited="true"' : '' ?>>
        </div>
        </div>
        <div class="form-group full">
            <button type="submit" class="button-primary">Entronizar agora</button>
        </div>
    </form>
</section>
<section class="card">
    <h2>Como funciona?</h2>
    <p>Transformamos seus minutos diários de trono em horas trabalhadas do mês e multiplicamos pelo valor da sua hora líquida (art. 64 da CLT). Tudo considerando os dias úteis da sua escala real — nada de meses cheios injustos.</p>
    <ul>
        <li><strong>Escalas justas</strong>: 5x2, 6x1, 12x36 e 4x2 com presets fiéis.</li>
        <li><strong>Rankings por escala</strong>: compare-se com quem vive rotina parecida.</li>
        <li><strong>Privacidade real</strong>: a base nunca guarda salário, apenas derivados.</li>
    </ul>
</section>
<script>
    const scaleSelect = document.querySelector('[data-scale-select]');
    const hoursInput = document.querySelector('[data-hours-input]');
    if (scaleSelect && hoursInput) {
        const updateHours = () => {
            const option = scaleSelect.options[scaleSelect.selectedIndex];
            const defaultHours = option.getAttribute('data-default-hours');
            if (defaultHours && !hoursInput.dataset.userEdited) {
                hoursInput.value = defaultHours;
            }
        };
        if (hoursInput.dataset.userEdited === 'true') {
            hoursInput.dataset.userEdited = 'true';
        } else {
            hoursInput.addEventListener('input', () => {
                hoursInput.dataset.userEdited = 'true';
            }, { once: true });
        }
        scaleSelect.addEventListener('change', updateHours);
        updateHours();
    }
</script>
<?php include __DIR__ . '/../app/View/partials/footer.php';
