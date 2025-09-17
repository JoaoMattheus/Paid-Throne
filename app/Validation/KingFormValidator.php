<?php

declare(strict_types=1);

namespace App\Validation;

use App\Domain\SalaryType;
use App\Domain\WorkSchedule;
use App\DTO\KingData;

final class KingFormValidator
{
    /**
     * @param array<string, mixed> $input
     * @return array{data:KingData|null,errors:array<string,string>}
     */
    public function validate(array $input): array
    {
        $errors = [];

        $username = isset($input['username']) ? trim((string) $input['username']) : '';
        if ($username === '') {
            $errors['username'] = 'Não é de bom tom um rei não se apresentar!';
        } elseif (!preg_match('/^[a-zA-Z0-9]{5,15}$/', $username)) {
            $errors['username'] = 'Majestade, seu nome deve ter entre 5 e 15 caracteres, usando apenas letras e números.';
        }

        $averageBathroomTime = filter_var($input['averageBathroomTime'] ?? null, FILTER_VALIDATE_INT);
        if ($averageBathroomTime === false || $averageBathroomTime < 5) {
            $errors['averageBathroomTime'] = 'Majestade, o tempo no trono não deve ser menor que 5 minutos.';
        }

        $numberOfVisitsPerDay = filter_var($input['numberOfVisitsPerDay'] ?? null, FILTER_VALIDATE_INT);
        if ($numberOfVisitsPerDay === false || $numberOfVisitsPerDay < 1 || $numberOfVisitsPerDay > 5) {
            $errors['numberOfVisitsPerDay'] = 'Majestade, visite o trono entre 1 e 5 vezes por dia.';
        }

        $salaryRaw = str_replace([',', ' '], ['.', ''], (string) ($input['salary'] ?? ''));
        $salary = filter_var($salaryRaw, FILTER_VALIDATE_FLOAT);
        if ($salary === false || $salary < 1 || $salary > 50000) {
            $errors['salary'] = 'Majestade, informe um salário válido entre 1 e 50.000 moedas.';
        }

        $salaryType = isset($input['salaryType']) ? strtoupper((string) $input['salaryType']) : '';
        if (!SalaryType::exists($salaryType)) {
            $errors['salaryType'] = 'Majestade, selecione um tipo de salário válido.';
        }

        $workSchedule = isset($input['workSchedule']) ? strtoupper((string) $input['workSchedule']) : '';
        if (WorkSchedule::get($workSchedule) === null) {
            $errors['workSchedule'] = 'Majestade, escolha uma escala de trabalho válida.';
        }

        if (!isset($errors['averageBathroomTime'], $errors['numberOfVisitsPerDay'])
            && $averageBathroomTime !== false && $numberOfVisitsPerDay !== false) {
            $totalBathroomTime = $averageBathroomTime * $numberOfVisitsPerDay;
            if ($totalBathroomTime > 60) {
                $errors['numberOfVisitsPerDay'] = 'Vossa majestade deveria caminhar mais! O tempo total não pode exceder 60 minutos por dia.';
            }
        }

        if ($errors !== []) {
            return ['data' => null, 'errors' => $errors];
        }

        $king = new KingData(
            $username,
            (int) $averageBathroomTime,
            (int) $numberOfVisitsPerDay,
            (float) $salary,
            $salaryType,
            $workSchedule
        );

        return ['data' => $king, 'errors' => []];
    }
}
