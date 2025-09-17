<?php

declare(strict_types=1);

namespace App\Services;

use App\DTO\KingData;
use App\Domain\WorkSchedule;

final class SalaryCalculator
{
    public function calculateMinutesSpent(KingData $king, string $shiftTime): int
    {
        $minutesPerDay = $king->averageBathroomTime * $king->numberOfVisitsPerDay;

        return match ($shiftTime) {
            'DAILY' => $minutesPerDay,
            'MONTHLY' => $minutesPerDay * (WorkSchedule::get($king->workSchedule)['daysPerMonth'] ?? 0),
            'YEARLY' => $minutesPerDay * (WorkSchedule::get($king->workSchedule)['daysPerYear'] ?? 0),
            default => 0,
        };
    }

    public function calculateSalaryPerMinute(KingData $king): float
    {
        $schedule = WorkSchedule::get($king->workSchedule);
        if ($schedule === null) {
            return 0.0;
        }

        return match ($king->salaryType) {
            'HOURLY' => $king->salary / 60,
            'DAILY' => $king->salary / $schedule['minutesPerShift'],
            'MONTHLY' => $schedule['daysPerMonth'] > 0
                ? $king->salary / ($schedule['minutesPerShift'] * $schedule['daysPerMonth'])
                : 0.0,
            'YEARLY' => $schedule['daysPerYear'] > 0
                ? $king->salary / ($schedule['minutesPerShift'] * $schedule['daysPerYear'])
                : 0.0,
            default => 0.0,
        };
    }

    public function calculateTotalEarningsInBathroom(KingData $king, string $shiftTime): float
    {
        $salaryPerMinute = $this->calculateSalaryPerMinute($king);
        $minutesSpent = $this->calculateMinutesSpent($king, $shiftTime);

        return $salaryPerMinute * $minutesSpent;
    }
}
