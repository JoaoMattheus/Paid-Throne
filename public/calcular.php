<?php
require __DIR__ . '/../app/bootstrap.php';

use App\Domain\Calculator;
use App\Domain\Scales;
use App\Infra\SubmissionRepo;
use App\Support\InputSanitizer;
use App\Support\Security;

session_start();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    header('Location: /');
    exit;
}

try {
    $nickname = trim((string) filter_input(INPUT_POST, 'apelido', FILTER_SANITIZE_SPECIAL_CHARS));
    $salaryRaw = (string) filter_input(INPUT_POST, 'salario');
    $minutesRaw = (string) filter_input(INPUT_POST, 'minutos');
    $scale = (string) filter_input(INPUT_POST, 'escala', FILTER_SANITIZE_SPECIAL_CHARS);
    $hoursRaw = (string) filter_input(INPUT_POST, 'horas');

    Security::ensureNicknameIsSafe($nickname);

    $scales = Scales::presets();
    if (!isset($scales[$scale])) {
        throw new RuntimeException('Escala desconhecida.');
    }

    $salary = InputSanitizer::parseMoney($salaryRaw);
    if ($salary < 1 || $salary > 1000000) {
        throw new RuntimeException('Salário fora da faixa aceitável (R$ 1 a R$ 1.000.000).');
    }

    $minutesPerDay = InputSanitizer::parseInt($minutesRaw);
    if ($minutesPerDay < 1 || $minutesPerDay > 120) {
        throw new RuntimeException('Minutos por dia devem estar entre 1 e 120.');
    }

    $hoursPerDay = InputSanitizer::parseFloat($hoursRaw ?: (string) $scales[$scale]['default_hours_per_day']);
    if ($hoursPerDay < 1 || $hoursPerDay > 24) {
        throw new RuntimeException('Horas por dia devem estar entre 1 e 24.');
    }

    $ipHash = Security::hashIp($_SERVER['REMOTE_ADDR'] ?? '', $_SERVER['HTTP_USER_AGENT'] ?? '');
    Security::ensureRateLimit($ipHash);

    $calculator = new Calculator();
    $results = $calculator->calculate($salary, $minutesPerDay, $scale, $hoursPerDay);

    $repo = new SubmissionRepo();
    $payload = array_merge($results, [
        'nickname' => $nickname,
        'ip_hash' => $ipHash,
        'user_agent' => substr((string) ($_SERVER['HTTP_USER_AGENT'] ?? ''), 0, 255),
    ]);
    $submissionId = $repo->store($payload);

    $_SESSION['last_result'] = [
        'id' => $submissionId,
        'nickname' => $nickname,
        'hour_value' => $results['hour_value'],
        'throne_hours_month' => $results['throne_hours_month'],
    ];

    header('Location: /resultado.php?id=' . $submissionId);
    exit;
} catch (\Throwable $exception) {
    $_SESSION['form_error'] = $exception->getMessage();
    $_SESSION['form_old'] = [
        'apelido' => $nickname ?? '',
        'salario' => $salaryRaw ?? '',
        'minutos' => $minutesRaw ?? '',
        'escala' => $scale ?? '5x2',
        'horas' => $hoursRaw ?? '',
    ];
    header('Location: /');
    exit;
}
