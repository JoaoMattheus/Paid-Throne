<?php

declare(strict_types=1);

namespace App\DTO;

final class CalculationResult
{
    public function __construct(
        public readonly int $dailyMinutesSpent,
        public readonly int $monthlyMinutesSpent,
        public readonly int $yearlyMinutesSpent,
        public readonly float $dailyEarnings,
        public readonly float $monthlyEarnings,
        public readonly float $yearlyEarnings,
        public readonly float $dailyShiftPercentage
    ) {
    }
}
