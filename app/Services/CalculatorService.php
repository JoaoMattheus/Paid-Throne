<?php

declare(strict_types=1);

namespace App\Services;

use App\DTO\CalculationResult;
use App\DTO\KingData;
use App\Domain\WorkSchedule;

final class CalculatorService
{
    public function __construct(
        private readonly SalaryCalculator $salaryCalculator = new SalaryCalculator(),
        private readonly BathroomCalculator $bathroomCalculator = new BathroomCalculator(),
    ) {
    }

    public function calculate(KingData $king): CalculationResult
    {
        $dailyMinutes = $this->salaryCalculator->calculateMinutesSpent($king, 'DAILY');
        $monthlyMinutes = $this->salaryCalculator->calculateMinutesSpent($king, 'MONTHLY');
        $yearlyMinutes = $this->salaryCalculator->calculateMinutesSpent($king, 'YEARLY');

        $breakdowns = $this->salaryCalculator->calculateBreakdowns($king);

        $minutesWorked = WorkSchedule::getMinutesWorked($king->workSchedule, 'DAILY');
        $dailyPercentage = round($this->bathroomCalculator->calculateDailyPercentageOfShift(
            $king->averageBathroomTime,
            $king->numberOfVisitsPerDay,
            $minutesWorked
        ), 2);

        return new CalculationResult(
            $dailyMinutes,
            $monthlyMinutes,
            $yearlyMinutes,
            $breakdowns['DAILY'],
            $breakdowns['MONTHLY'],
            $breakdowns['YEARLY'],
            $dailyPercentage
        );
    }
}
