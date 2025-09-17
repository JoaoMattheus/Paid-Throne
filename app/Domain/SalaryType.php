<?php

declare(strict_types=1);

namespace App\Domain;

final class SalaryType
{
    /** @var array<string, array<string, string>> */
    private const TYPES = [
        'HOURLY' => [
            'label' => 'Salário por hora',
            'description' => 'Ideal para profissionais com jornada variável. Informe o valor bruto da sua hora de trabalho.'
        ],
        'DAILY' => [
            'label' => 'Salário por dia',
            'description' => 'Utilizado em regimes de diária. Informe quanto recebe por dia completo de trabalho.'
        ],
        'MONTHLY' => [
            'label' => 'Salário mensal',
            'description' => 'Modelo tradicional CLT. Informe o valor bruto recebido todo mês.'
        ],
        'YEARLY' => [
            'label' => 'Salário anual',
            'description' => 'Comum em cargos executivos. Informe o valor total recebido no ano.'
        ],
    ];

    public static function all(): array
    {
        return self::TYPES;
    }

    public static function exists(string $key): bool
    {
        return array_key_exists($key, self::TYPES);
    }
}
