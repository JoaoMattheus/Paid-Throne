<?php

declare(strict_types=1);

namespace App\Tests\Domain;

use App\Domain\Calculator;
use InvalidArgumentException;
use PHPUnit\Framework\TestCase;

class CalculatorTest extends TestCase
{
    public function testCalculatesFor5x2Scale(): void
    {
        $calculator = new Calculator();
        $result = $calculator->calculate(3000.00, 15, '5x2', 8.8);

        $expectedDaysMonth = 5 * 4.333;
        $expectedWorkHours = (5 * 8.8) * 4.333;
        $expectedMinutes = (int) round(15 * $expectedDaysMonth);
        $hourValue = 3000.00 / $expectedWorkHours;
        $expectedMoneyMonth = round(($expectedMinutes / 60) * $hourValue, 2);

        $this->assertEqualsWithDelta(round($expectedDaysMonth, 2), $result['days_per_month'], 0.01);
        $this->assertEqualsWithDelta(round($expectedWorkHours, 2), $result['work_hours_per_month'], 0.01);
        $this->assertSame($expectedMinutes, $result['throne_minutes_month']);
        $this->assertEqualsWithDelta($expectedMoneyMonth, $result['throne_money_month'], 0.01);
        $this->assertEqualsWithDelta($expectedMoneyMonth * 12, $result['throne_money_year'], 0.05);
    }

    public function testCalculatesFor6x1Scale(): void
    {
        $calculator = new Calculator();
        $result = $calculator->calculate(2500.00, 12, '6x1', 7.33);

        $expectedDaysMonth = 6 * 4.333;
        $expectedWorkHours = (6 * 7.33) * 4.333;
        $expectedMinutes = (int) round(12 * $expectedDaysMonth);
        $hourValue = 2500.00 / $expectedWorkHours;
        $expectedMoneyMonth = round(($expectedMinutes / 60) * $hourValue, 2);

        $this->assertEqualsWithDelta(round($expectedDaysMonth, 2), $result['days_per_month'], 0.01);
        $this->assertEqualsWithDelta(round($expectedWorkHours, 2), $result['work_hours_per_month'], 0.01);
        $this->assertSame($expectedMinutes, $result['throne_minutes_month']);
        $this->assertEqualsWithDelta($expectedMoneyMonth, $result['throne_money_month'], 0.01);
    }

    public function testCalculatesFor12x36Scale(): void
    {
        $calculator = new Calculator();
        $result = $calculator->calculate(3200.00, 10, '12x36', 12.0);

        $expectedDaysMonth = 30.4375 * 0.5;
        $expectedWorkHours = $expectedDaysMonth * 12.0;
        $expectedMinutes = (int) round(10 * $expectedDaysMonth);
        $hourValue = 3200.00 / $expectedWorkHours;
        $expectedMoneyMonth = round(($expectedMinutes / 60) * $hourValue, 2);

        $this->assertEqualsWithDelta(round($expectedDaysMonth, 2), $result['days_per_month'], 0.01);
        $this->assertEqualsWithDelta(round($expectedWorkHours, 2), $result['work_hours_per_month'], 0.01);
        $this->assertSame($expectedMinutes, $result['throne_minutes_month']);
        $this->assertEqualsWithDelta($expectedMoneyMonth, $result['throne_money_month'], 0.01);
    }

    public function testCalculatesFor4x2Scale(): void
    {
        $calculator = new Calculator();
        $result = $calculator->calculate(2800.00, 8, '4x2', 8.0);

        $expectedDaysMonth = 30.4375 * (4.0 / 6.0);
        $expectedWorkHours = $expectedDaysMonth * 8.0;
        $expectedMinutes = (int) round(8 * $expectedDaysMonth);
        $hourValue = 2800.00 / $expectedWorkHours;
        $expectedMoneyMonth = round(($expectedMinutes / 60) * $hourValue, 2);

        $this->assertEqualsWithDelta(round($expectedDaysMonth, 2), $result['days_per_month'], 0.01);
        $this->assertEqualsWithDelta(round($expectedWorkHours, 2), $result['work_hours_per_month'], 0.01);
        $this->assertSame($expectedMinutes, $result['throne_minutes_month']);
        $this->assertEqualsWithDelta($expectedMoneyMonth, $result['throne_money_month'], 0.01);
    }

    public function testRejectsInvalidSalary(): void
    {
        $this->expectException(InvalidArgumentException::class);
        $calculator = new Calculator();
        $calculator->calculate(0.0, 10, '5x2', 8.8);
    }
}
