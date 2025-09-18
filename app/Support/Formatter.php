<?php

namespace App\Support;

use NumberFormatter;

class Formatter
{
    public static function money(float $value): string
    {
        $formatter = new NumberFormatter('pt_BR', NumberFormatter::CURRENCY);
        $formatter->setAttribute(NumberFormatter::FRACTION_DIGITS, 2);
        return $formatter->formatCurrency($value, 'BRL');
    }

    public static function minutesToHuman(int $minutes): string
    {
        $hours = intdiv($minutes, 60);
        $remaining = $minutes % 60;
        if ($hours === 0) {
            return sprintf('%d min', $minutes);
        }
        if ($remaining === 0) {
            return sprintf('%dh', $hours);
        }
        return sprintf('%dh%02d', $hours, $remaining);
    }

    public static function humanizeScale(string $code): string
    {
        $labels = [
            '5x2' => '5x2',
            '6x1' => '6x1',
            '12x36' => '12x36',
            '4x2' => '4x2',
        ];

        return $labels[$code] ?? $code;
    }
}
