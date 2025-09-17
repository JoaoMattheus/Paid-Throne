<?php

declare(strict_types=1);

namespace App\DTO;

final class CalculationResult
{
    public function __construct(
        public readonly int $dailyMinutesSpent,
        public readonly int $monthlyMinutesSpent,
        public readonly int $yearlyMinutesSpent,
        public readonly SalaryBreakdown $dailySalary,
        public readonly SalaryBreakdown $monthlySalary,
        public readonly SalaryBreakdown $yearlySalary,
        public readonly float $dailyShiftPercentage
    ) {
    }
}
