<?php
require_once __DIR__ . '/../bootstrap.php';

use App\Domain\SalaryType;
use App\Domain\WorkSchedule;
use App\Services\CalculatorService;
use App\Validation\KingFormValidator;

$pageTitle = 'Calculadora de Cagada Remunerada | Trono Remunerado';
$metaTitle = $pageTitle;
$metaDescription = 'Descubra quanto vale cada ida ao banheiro durante o expediente e otimize seu tempo com a calculadora oficial do Trono Remunerado.';
$canonical = (string) env('APP_URL', '') !== '' ? rtrim((string) env('APP_URL', ''), '/') . '/' : '/';

$validator = new KingFormValidator();
$calculator = new CalculatorService();

$input = [
    'username' => '',
    'averageBathroomTime' => '',
    'numberOfVisitsPerDay' => '',
    'salary' => '',
    'salaryType' => 'MONTHLY',
    'workSchedule' => 'FIVE_ON_TWO',
];
$errors = [];
$result = null;
$adsenseSlot = env('ADSENSE_CALCULATOR_SLOT_ID');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = [
        'username' => $_POST['username'] ?? '',
        'averageBathroomTime' => $_POST['averageBathroomTime'] ?? '',
        'numberOfVisitsPerDay' => $_POST['numberOfVisitsPerDay'] ?? '',
        'salary' => $_POST['salary'] ?? '',
        'salaryType' => $_POST['salaryType'] ?? '',
        'workSchedule' => $_POST['workSchedule'] ?? '',
    ];

    $validation = $validator->validate($input);
    $errors = $validation['errors'];

    if ($validation['data'] !== null) {
        $result = $calculator->calculate($validation['data']);
    }
}

$salaryTypes = SalaryType::all();
$workSchedules = WorkSchedule::all();
$salaryHints = [];
foreach ($salaryTypes as $key => $type) {
    $salaryHints[$key] = $type['description'];
}
$scheduleHints = [];
foreach ($workSchedules as $key => $schedule) {
    $scheduleHints[$key] = $schedule['description'];
}

require BASE_PATH . '/templates/partials/header.php';
?>
<section class="hero">
    <div class="container hero__grid">
        <div class="hero__content">
            <h1>O tempo no trono agora rende: calcule seu lucro real</h1>
            <p>
                O Trono Remunerado transforma o seu intervalo mais íntimo em dados divertidos. 
                Descubra quanto do seu salário é ganho no banheiro, compartilhe com os colegas e suba no ranking da corte.
            </p>
            <ul class="hero__bullets">
                <li>Mobile first, pensado para usar no caminho do banheiro.</li>
                <li>Resultados claros com projeções diárias, mensais e anuais.</li>
                <li>Regras oficiais do reino: limite de 60 minutos por dia no trono.</li>
            </ul>
        </div>
        <div class="hero__card">
            <form method="post" novalidate class="calculator-form" aria-label="Calculadora de tempo remunerado no banheiro">
                <div class="form-group">
                    <label for="username">Nome real</label>
                    <input type="text" id="username" name="username" required minlength="5" maxlength="15" pattern="[A-Za-z0-9]+"
                           value="<?= sanitize_output((string) $input['username']); ?>" placeholder="Sua Majestade">
                    <?php if (isset($errors['username'])): ?>
                        <p class="form-error"><?= sanitize_output($errors['username']); ?></p>
                    <?php endif; ?>
                </div>
                <div class="form-group">
                    <label for="averageBathroomTime">Tempo médio por visita (min)</label>
                    <input type="number" id="averageBathroomTime" name="averageBathroomTime" min="5" max="60" step="1" required
                           value="<?= sanitize_output((string) $input['averageBathroomTime']); ?>">
                    <?php if (isset($errors['averageBathroomTime'])): ?>
                        <p class="form-error"><?= sanitize_output($errors['averageBathroomTime']); ?></p>
                    <?php endif; ?>
                </div>
                <div class="form-group">
                    <label for="numberOfVisitsPerDay">Visitas por dia</label>
                    <input type="number" id="numberOfVisitsPerDay" name="numberOfVisitsPerDay" min="1" max="5" step="1" required
                           value="<?= sanitize_output((string) $input['numberOfVisitsPerDay']); ?>">
                    <?php if (isset($errors['numberOfVisitsPerDay'])): ?>
                        <p class="form-error"><?= sanitize_output($errors['numberOfVisitsPerDay']); ?></p>
                    <?php endif; ?>
                </div>
                <div class="form-group">
                    <label for="salary">Salário</label>
                    <input type="number" id="salary" name="salary" min="1" max="50000" step="0.01" required
                           value="<?= sanitize_output((string) $input['salary']); ?>" placeholder="2500.00">
                    <?php if (isset($errors['salary'])): ?>
                        <p class="form-error"><?= sanitize_output($errors['salary']); ?></p>
                    <?php endif; ?>
                </div>
                <div class="form-group">
                    <label for="salaryType">Tipo de salário</label>
                    <select id="salaryType" name="salaryType" required data-hints='<?= sanitize_output(json_encode($salaryHints, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES)); ?>'>
                        <?php foreach ($salaryTypes as $key => $salaryType): ?>
                            <option value="<?= sanitize_output($key); ?>" <?= ($input['salaryType'] === $key ? 'selected' : ''); ?>>
                                <?= sanitize_output($salaryType['label']); ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                    <p class="form-hint" data-salary-hint></p>
                    <?php if (isset($errors['salaryType'])): ?>
                        <p class="form-error"><?= sanitize_output($errors['salaryType']); ?></p>
                    <?php endif; ?>
                </div>
                <div class="form-group">
                    <label for="workSchedule">Escala de trabalho</label>
                    <select id="workSchedule" name="workSchedule" required data-hints='<?= sanitize_output(json_encode($scheduleHints, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES)); ?>'>
                        <?php foreach ($workSchedules as $key => $schedule): ?>
                            <option value="<?= sanitize_output($key); ?>" <?= ($input['workSchedule'] === $key ? 'selected' : ''); ?>>
                                <?= sanitize_output($schedule['label']); ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                    <p class="form-hint" data-schedule-hint></p>
                    <?php if (isset($errors['workSchedule'])): ?>
                        <p class="form-error"><?= sanitize_output($errors['workSchedule']); ?></p>
                    <?php endif; ?>
                </div>
                <button type="submit" class="button button--primary">Calcular meu trono</button>
                <p class="form-disclaimer">Nunca armazenamos seus dados pessoais. Os cálculos acontecem aqui mesmo no seu navegador.</p>
            </form>
        </div>
    </div>
</section>
<?php if ($adsenseSlot): ?>
<section class="ads-section" aria-label="Publicidade">
    <div class="container">
        <ins class="adsbygoogle"
             style="display:block"
             data-ad-client="<?= sanitize_output((string) env('ADSENSE_CLIENT_ID')); ?>"
             data-ad-slot="<?= sanitize_output($adsenseSlot); ?>"
             data-ad-format="auto"
             data-full-width-responsive="true"></ins>
        <script>
            (adsbygoogle = window.adsbygoogle || []).push({});
        </script>
    </div>
</section>
<?php endif; ?>
<section class="results" id="resultado" aria-live="polite">
    <div class="container">
        <h2>Resumo real da sua jornada</h2>
        <?php if ($result !== null): ?>
            <div class="results__grid">
                <article class="result-card">
                    <h3>Tempo de trono</h3>
                    <ul>
                        <li><strong>Diário:</strong> <?= sanitize_output((string) $result->dailyMinutesSpent); ?> minutos</li>
                        <li><strong>Mensal:</strong> <?= sanitize_output((string) $result->monthlyMinutesSpent); ?> minutos</li>
                        <li><strong>Anual:</strong> <?= sanitize_output((string) $result->yearlyMinutesSpent); ?> minutos</li>
                    </ul>
                </article>
                <article class="result-card">
                    <h3>Ganhos no trono</h3>
                    <ul>
                        <li>
                            <strong>Diário:</strong>
                            <?= sanitize_output(format_currency($result->dailySalary->netEarnings)); ?> líquidos
                            (<?= sanitize_output(format_currency($result->dailySalary->grossEarnings)); ?> brutos)
                        </li>
                        <li>
                            <strong>Mensal:</strong>
                            <?= sanitize_output(format_currency($result->monthlySalary->netEarnings)); ?> líquidos
                            (<?= sanitize_output(format_currency($result->monthlySalary->grossEarnings)); ?> brutos)
                        </li>
                        <li>
                            <strong>Anual:</strong>
                            <?= sanitize_output(format_currency($result->yearlySalary->netEarnings)); ?> líquidos
                            (<?= sanitize_output(format_currency($result->yearlySalary->grossEarnings)); ?> brutos)
                        </li>
                    </ul>
                </article>
                <article class="result-card">
                    <h3>Tributos proporcionais</h3>
                    <ul>
                        <li>
                            <strong>Diário:</strong>
                            INSS <?= sanitize_output(format_currency($result->dailySalary->inss)); ?> ·
                            IRRF <?= sanitize_output(format_currency($result->dailySalary->irrf)); ?>
                        </li>
                        <li>
                            <strong>Mensal:</strong>
                            INSS <?= sanitize_output(format_currency($result->monthlySalary->inss)); ?> ·
                            IRRF <?= sanitize_output(format_currency($result->monthlySalary->irrf)); ?>
                        </li>
                        <li>
                            <strong>Anual:</strong>
                            INSS <?= sanitize_output(format_currency($result->yearlySalary->inss)); ?> ·
                            IRRF <?= sanitize_output(format_currency($result->yearlySalary->irrf)); ?>
                        </li>
                    </ul>
                    <p class="result-card__note">Valores proporcionais à parte do salário conquistada no trono.</p>
                </article>
                <article class="result-card">
                    <h3>Impacto no expediente</h3>
                    <p>Você passa <?= sanitize_output(format_percentage($result->dailyShiftPercentage)); ?> da sua jornada diária no trono.</p>
                    <p>Equilíbrio é tudo: manter-se abaixo de 60 minutos por dia é a regra de ouro do reino.</p>
                </article>
            </div>
        <?php else: ?>
            <p class="results__empty">Preencha os dados acima para descobrir seu desempenho real no trono.</p>
        <?php endif; ?>
    </div>
</section>
<section class="seo-section">
    <div class="container">
        <h2>Como funciona o Trono Remunerado</h2>
        <div class="seo-section__grid">
            <article>
                <h3>Regras do reino</h3>
                <p>
                    Nosso cálculo considera seu tempo médio no banheiro e o número de visitas por dia, sempre respeitando o limite de
                    60 minutos diários. É assim que evitamos abusos dignos de um golpe de estado.
                </p>
                <p>
                    Combinamos os diferentes tipos de salário e escalas de trabalho para oferecer projeções realistas e prontas para
                    compartilhar com a equipe.
                </p>
            </article>
            <article>
                <h3>Escalas suportadas</h3>
                <ul class="schedule-list">
                    <?php foreach ($workSchedules as $schedule): ?>
                        <li>
                            <strong><?= sanitize_output($schedule['label']); ?>:</strong>
                            <?= sanitize_output($schedule['description']); ?>
                        </li>
                    <?php endforeach; ?>
                </ul>
            </article>
            <article>
                <h3>Por que isso é útil?</h3>
                <p>
                    Além da diversão, o Trono Remunerado ajuda a entender como pequenas pausas influenciam produtividade, bem-estar e
                    até a ergonomia do escritório. Use os dados para negociar pausas, ajustar turnos e planejar o café com a equipe.
                </p>
            </article>
        </div>
    </div>
</section>
<section class="faq" id="faq">
    <div class="container">
        <h2>Perguntas que todo rei já fez</h2>
        <details>
            <summary>Vocês salvam meus dados?</summary>
            <p>Não. Todo cálculo acontece no seu navegador, sem cadastro e sem cookies invasivos. Transparência total com a realeza.</p>
        </details>
        <details>
            <summary>Como posso monetizar com Adsense?</summary>
            <p>Configure as variáveis <code>ADSENSE_CLIENT_ID</code> e <code>ADSENSE_CALCULATOR_SLOT_ID</code> para exibir os anúncios automáticos do Google.</p>
        </details>
        <details>
            <summary>Quais navegadores são suportados?</summary>
            <p>O site é mobile first e utiliza apenas recursos modernos suportados por todos os navegadores atuais.</p>
        </details>
    </div>
</section>
<?php require BASE_PATH . '/templates/partials/footer.php'; ?>
