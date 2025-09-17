<?php

declare(strict_types=1);

namespace App\DTO;

final class KingData
{
    public function __construct(
        public readonly string $username,
        public readonly int $averageBathroomTime,
        public readonly int $numberOfVisitsPerDay,
        public readonly float $salary,
        public readonly string $salaryType,
        public readonly string $workSchedule
    ) {
    }
}
