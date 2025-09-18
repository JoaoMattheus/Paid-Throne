<?php

namespace App\Domain;

use InvalidArgumentException;

class Calculator
{
    /**
     * @param float $salary Netto salary (not persisted)
     * @param int $minutesPerDay Minutes spent on the throne per working day
     * @param string $scaleCode One of the supported scales
     * @param float|null $customHoursPerDay Optional override for hours per working day
     * @return array<string, float|int|string>
     */
    public function calculate(float $salary, int $minutesPerDay, string $scaleCode, ?float $customHoursPerDay = null): array
    {
        if ($salary <= 0.0) {
            throw new InvalidArgumentException('Salário deve ser maior que zero.');
        }

        if ($minutesPerDay < 1) {
            throw new InvalidArgumentException('Minutos por dia deve ser positivo.');
        }

        $scale = Scales::get($scaleCode);
        $hoursPerDay = $customHoursPerDay !== null ? max(1.0, min(24.0, $customHoursPerDay)) : (float) $scale['default_hours_per_day'];
        $daysPerWeekPreset = $scale['days_per_week'] !== null ? (float) $scale['days_per_week'] : null;

        $daysPerMonth = Scales::daysPerMonth($scaleCode, $daysPerWeekPreset);
        $workHoursPerMonth = Scales::workHoursPerMonth($scaleCode, $hoursPerDay, $daysPerWeekPreset);

        if ($workHoursPerMonth <= 0.0) {
            throw new InvalidArgumentException('Horas trabalhadas no mês inválidas.');
        }

        $hourValue = $salary / $workHoursPerMonth;
        $throneMinutesMonth = $minutesPerDay * $daysPerMonth;
        $throneHoursMonth = $throneMinutesMonth / 60.0;
        $throneMinutesYear = $throneMinutesMonth * 12;
        $moneyMonth = round($throneHoursMonth * $hourValue, 2);
        $moneyYear = round($moneyMonth * 12, 2);

        return [
            'scale_code' => $scaleCode,
            'minutes_per_day' => $minutesPerDay,
            'days_per_week' => $daysPerWeekPreset !== null
                ? $daysPerWeekPreset
                : round($daysPerMonth / 4.333, 2),
            'hours_per_day' => $hoursPerDay,
            'days_per_month' => round($daysPerMonth, 2),
            'work_hours_per_month' => round($workHoursPerMonth, 2),
            'throne_minutes_month' => (int) round($throneMinutesMonth),
            'throne_minutes_year' => (int) round($throneMinutesYear),
            'throne_hours_month' => $throneHoursMonth,
            'hour_value' => $hourValue,
            'throne_money_month' => $moneyMonth,
            'throne_money_year' => $moneyYear,
        ];
    }
}
