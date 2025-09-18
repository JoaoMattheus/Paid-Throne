<?php

namespace App\Support;

use NumberFormatter;
use RuntimeException;

class InputSanitizer
{
    public static function parseMoney(string $value): float
    {
        $formatter = new NumberFormatter('pt_BR', NumberFormatter::CURRENCY);
        $clean = trim($value);
        $currency = null;
        $parsed = $formatter->parseCurrency($clean, $currency);
        if ($parsed === false) {
            $formatter = new NumberFormatter('pt_BR', NumberFormatter::DECIMAL);
            $parsed = $formatter->parse($clean);
        }
        if ($parsed === false) {
            throw new RuntimeException('Informe o salário líquido em reais.');
        }

        return (float) $parsed;
    }

    public static function parseInt(string $value): int
    {
        $int = filter_var($value, FILTER_VALIDATE_INT);
        if ($int === false) {
            throw new RuntimeException('Valor inteiro inválido.');
        }

        return (int) $int;
    }

    public static function parseFloat(string $value): float
    {
        $normalized = str_replace(',', '.', $value);
        if (!is_numeric($normalized)) {
            throw new RuntimeException('Número inválido.');
        }

        return (float) $normalized;
    }
}
