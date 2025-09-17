<?php

declare(strict_types=1);

namespace App\Domain;

final class WorkSchedule
{
    /** @var array<string, array<string, mixed>> */
    private const SCHEDULES = [
        'SIX_ON_ONE' => [
            'label' => 'Escala 6x1',
            'description' => 'Trabalha seis dias e folga um, comum em varejo e serviços.',
            'daysPerWeek' => 6,
            'daysPerMonth' => 26,
            'daysPerYear' => 312,
            'minutesPerShift' => 440,
        ],
        'FIVE_ON_TWO' => [
            'label' => 'Escala 5x2',
            'description' => 'Jornada tradicional de segunda a sexta com folga aos fins de semana.',
            'daysPerWeek' => 5,
            'daysPerMonth' => 20,
            'daysPerYear' => 240,
            'minutesPerShift' => 480,
        ],
        'FOUR_ON_THREE' => [
            'label' => 'Escala 4x3',
            'description' => 'Quatro dias de trabalho com três de descanso, comum em tecnologia e logística.',
            'daysPerWeek' => 4,
            'daysPerMonth' => 16,
            'daysPerYear' => 192,
            'minutesPerShift' => 480,
        ],
        'TWELVE_ON_THIRTY_SIX' => [
            'label' => 'Plantonista 12x36',
            'description' => 'Doze horas trabalhadas e trinta e seis de descanso, típica da área da saúde.',
            'daysPerWeek' => 3,
            'daysPerMonth' => 12,
            'daysPerYear' => 144,
            'minutesPerShift' => 720,
        ],
    ];

    public static function all(): array
    {
        return self::SCHEDULES;
    }

    public static function get(string $key): ?array
    {
        return self::SCHEDULES[$key] ?? null;
    }

    public static function getMinutesWorked(string $key, string $shiftTime): int
    {
        $schedule = self::get($key);
        if ($schedule === null) {
            return 0;
        }

        return match ($shiftTime) {
            'DAILY' => (int) $schedule['minutesPerShift'],
            'MONTHLY' => (int) $schedule['minutesPerShift'] * (int) $schedule['daysPerMonth'],
            'YEARLY' => (int) $schedule['minutesPerShift'] * (int) $schedule['daysPerYear'],
            default => 0,
        };
    }
}
