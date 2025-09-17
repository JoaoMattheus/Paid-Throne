<?php

declare(strict_types=1);

namespace App\Services;

final class TaxCalculator
{
    /**
     * INSS progressive brackets for 2024.
     * @var array<int, array{limit: float, rate: float}>
     */
    private const INSS_BRACKETS = [
        ['limit' => 1412.00, 'rate' => 0.075],
        ['limit' => 2666.68, 'rate' => 0.09],
        ['limit' => 4000.03, 'rate' => 0.12],
        ['limit' => 7786.02, 'rate' => 0.14],
    ];

    /**
     * IRRF table with the simplified discount already considered (2024).
     * @var array<int, array{limit: float|null, rate: float, deduction: float}>
     */
    private const IRRF_TABLE = [
        ['limit' => 2259.20, 'rate' => 0.0, 'deduction' => 0.0],
        ['limit' => 2826.65, 'rate' => 0.075, 'deduction' => 169.44],
        ['limit' => 3751.05, 'rate' => 0.15, 'deduction' => 381.44],
        ['limit' => 4664.68, 'rate' => 0.225, 'deduction' => 662.77],
        ['limit' => null, 'rate' => 0.275, 'deduction' => 896.00],
    ];

    private const IRRF_SIMPLIFIED_DEDUCTION = 528.00;

    /**
     * @return array{inss: float, irrf: float}
     */
    public function calculateMonthlyTaxes(float $monthlySalary): array
    {
        if ($monthlySalary <= 0) {
            return ['inss' => 0.0, 'irrf' => 0.0];
        }

        $inss = $this->calculateInss($monthlySalary);
        $taxableBase = max(0.0, $monthlySalary - $inss - self::IRRF_SIMPLIFIED_DEDUCTION);
        $irrf = $this->calculateIrrf($taxableBase);

        return [
            'inss' => $inss,
            'irrf' => $irrf,
        ];
    }

    private function calculateInss(float $monthlySalary): float
    {
        $previousLimit = 0.0;
        $total = 0.0;

        foreach (self::INSS_BRACKETS as $bracket) {
            $limit = $bracket['limit'];
            $rate = $bracket['rate'];

            if ($monthlySalary <= $previousLimit) {
                break;
            }

            $portion = min($monthlySalary, $limit) - $previousLimit;
            if ($portion > 0) {
                $total += $portion * $rate;
            }

            $previousLimit = $limit;
        }

        return $total;
    }

    private function calculateIrrf(float $taxableBase): float
    {
        if ($taxableBase <= 0) {
            return 0.0;
        }

        foreach (self::IRRF_TABLE as $bracket) {
            $limit = $bracket['limit'];
            $rate = $bracket['rate'];
            $deduction = $bracket['deduction'];

            if ($limit !== null && $taxableBase > $limit) {
                continue;
            }

            $value = ($taxableBase * $rate) - $deduction;
            return max(0.0, $value);
        }

        return 0.0;
    }
}
