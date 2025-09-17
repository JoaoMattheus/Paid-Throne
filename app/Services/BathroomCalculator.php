<?php

declare(strict_types=1);

namespace App\Services;

final class BathroomCalculator
{
    public function calculateDailyPercentageOfShift(int $averageBathroomTime, int $numberOfVisitsPerDay, int $minutesWorked): float
    {
        if ($averageBathroomTime <= 0 || $numberOfVisitsPerDay <= 0 || $minutesWorked <= 0) {
            return 0.0;
        }

        $percentage = ($averageBathroomTime * $numberOfVisitsPerDay * 100) / $minutesWorked;

        return min(100, $percentage);
    }
}
