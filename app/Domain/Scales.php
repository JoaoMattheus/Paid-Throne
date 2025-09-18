<?php

namespace App\Domain;

use InvalidArgumentException;

class Scales
{
    public const SCALE_VERSION = 1;

    /**
     * @return array<string, array<string, mixed>>
     */
    public static function presets(): array
    {
        return [
            '5x2' => [
                'label' => '5x2 - expediente comum',
                'days_per_week' => 5.0,
                'default_hours_per_day' => 8.8,
                'type' => 'weekly',
                'legal_note' => '8h/44h CLT',
            ],
            '6x1' => [
                'label' => '6x1 - comércio/serviços',
                'days_per_week' => 6.0,
                'default_hours_per_day' => 7.33,
                'type' => 'weekly',
                'legal_note' => 'Limite 44h semanais',
            ],
            '12x36' => [
                'label' => '12x36 - plantões art. 59-A',
                'days_per_week' => null,
                'default_hours_per_day' => 12.0,
                'type' => 'cyclical',
                'cycle_ratio' => 0.5,
                'legal_note' => 'Prevista no art. 59-A da CLT',
            ],
            '4x2' => [
                'label' => '4x2 - operações contínuas',
                'days_per_week' => null,
                'default_hours_per_day' => 8.0,
                'type' => 'cyclical',
                'cycle_ratio' => 4 / 6,
                'legal_note' => 'Escala comum em operações contínuas',
            ],
        ];
    }

    /**
     * @return array<string, mixed>
     */
    public static function get(string $code): array
    {
        $presets = self::presets();

        if (!isset($presets[$code])) {
            throw new InvalidArgumentException('Escala desconhecida.');
        }

        return $presets[$code];
    }

    /**
     * Determina os dias trabalhados no mês para uma escala.
     */
    public static function daysPerMonth(string $code, float $daysPerWeek = null): float
    {
        if ($code === '5x2' || $code === '6x1') {
            $presets = self::get($code);
            $effectiveDays = $daysPerWeek ?? (float) $presets['days_per_week'];
            return $effectiveDays * 4.333;
        }

        if ($code === '12x36') {
            return 30.4375 * 0.5;
        }

        if ($code === '4x2') {
            return 30.4375 * (4.0 / 6.0);
        }

        throw new InvalidArgumentException('Escala desconhecida.');
    }

    public static function workHoursPerMonth(string $code, float $hoursPerDay, float $daysPerWeek = null): float
    {
        if ($code === '5x2' || $code === '6x1') {
            $presets = self::get($code);
            $effectiveDays = $daysPerWeek ?? (float) $presets['days_per_week'];
            return ($effectiveDays * $hoursPerDay) * 4.333;
        }

        $daysPerMonth = self::daysPerMonth($code, $daysPerWeek);
        return $daysPerMonth * $hoursPerDay;
    }
}
