<?php

declare(strict_types=1);

namespace App\Services;

use App\DTO\KingData;
use App\DTO\SalaryBreakdown;
use App\Domain\WorkSchedule;

final class SalaryCalculator
{
    public function __construct(
        private readonly TaxCalculator $taxCalculator = new TaxCalculator(),
    ) {
    }

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

    /**
     * @return array<string, SalaryBreakdown>
     */
    public function calculateBreakdowns(KingData $king): array
    {
        $perMinuteValues = $this->calculatePerMinuteValues($king);
        $breakdowns = [];

        foreach (['DAILY', 'MONTHLY', 'YEARLY'] as $shiftTime) {
            $minutesSpent = $this->calculateMinutesSpent($king, $shiftTime);

            $breakdowns[$shiftTime] = new SalaryBreakdown(
                round($perMinuteValues['gross'] * $minutesSpent, 2),
                round($perMinuteValues['net'] * $minutesSpent, 2),
                round($perMinuteValues['inss'] * $minutesSpent, 2),
                round($perMinuteValues['irrf'] * $minutesSpent, 2),
            );
        }

        return $breakdowns;
    }

    /**
     * @return array{gross: float, net: float, inss: float, irrf: float}
     */
    private function calculatePerMinuteValues(KingData $king): array
    {
        $schedule = WorkSchedule::get($king->workSchedule);
        if ($schedule === null) {
            return ['gross' => 0.0, 'net' => 0.0, 'inss' => 0.0, 'irrf' => 0.0];
        }

        $minutesPerShift = (int) ($schedule['minutesPerShift'] ?? 0);
        $daysPerMonth = (int) ($schedule['daysPerMonth'] ?? 0);
        if ($minutesPerShift <= 0 || $daysPerMonth <= 0) {
            return ['gross' => 0.0, 'net' => 0.0, 'inss' => 0.0, 'irrf' => 0.0];
        }

        $monthlyMinutes = $minutesPerShift * $daysPerMonth;
        $monthlySalary = $this->calculateMonthlySalary($king, $schedule);
        if ($monthlyMinutes <= 0 || $monthlySalary <= 0) {
            return ['gross' => 0.0, 'net' => 0.0, 'inss' => 0.0, 'irrf' => 0.0];
        }

        $taxes = $this->taxCalculator->calculateMonthlyTaxes($monthlySalary);
        $monthlyNet = max(0.0, $monthlySalary - $taxes['inss'] - $taxes['irrf']);

        return [
            'gross' => $monthlySalary / $monthlyMinutes,
            'net' => $monthlyNet / $monthlyMinutes,
            'inss' => $taxes['inss'] / $monthlyMinutes,
            'irrf' => $taxes['irrf'] / $monthlyMinutes,
        ];
    }

    /**
     * @param array<string, mixed>|null $schedule
     */
    private function calculateMonthlySalary(KingData $king, ?array $schedule = null): float
    {
        $schedule ??= WorkSchedule::get($king->workSchedule);

        return match ($king->salaryType) {
            'HOURLY' => ($schedule !== null)
                ? $this->calculateHourlySalaryToMonthly($king->salary, $schedule)
                : 0.0,
            'DAILY' => ($schedule !== null)
                ? $this->calculateDailySalaryToMonthly($king->salary, $schedule)
                : 0.0,
            'MONTHLY' => max(0.0, $king->salary),
            'YEARLY' => max(0.0, $king->salary / 12),
            default => 0.0,
        };
    }

    /**
     * @param array<string, mixed> $schedule
     */
    private function calculateHourlySalaryToMonthly(float $salary, array $schedule): float
    {
        $minutesPerShift = (int) ($schedule['minutesPerShift'] ?? 0);
        $daysPerMonth = (int) ($schedule['daysPerMonth'] ?? 0);
        if ($minutesPerShift <= 0 || $daysPerMonth <= 0) {
            return 0.0;
        }

        $hoursPerShift = $minutesPerShift / 60;

        return max(0.0, $salary * $hoursPerShift * $daysPerMonth);
    }

    /**
     * @param array<string, mixed> $schedule
     */
    private function calculateDailySalaryToMonthly(float $salary, array $schedule): float
    {
        $daysPerMonth = (int) ($schedule['daysPerMonth'] ?? 0);
        if ($daysPerMonth <= 0) {
            return 0.0;
        }

        return max(0.0, $salary * $daysPerMonth);
    }
}
