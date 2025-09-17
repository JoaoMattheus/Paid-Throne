<?php

declare(strict_types=1);

namespace App\DTO;

final class SalaryBreakdown
{
    public function __construct(
        public readonly float $grossEarnings,
        public readonly float $netEarnings,
        public readonly float $inss,
        public readonly float $irrf
    ) {
    }
}
