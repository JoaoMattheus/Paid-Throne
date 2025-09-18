<?php

namespace App\Support;

use DateTimeImmutable;
use RuntimeException;

class Security
{
    private const RATE_LIMIT_SECONDS = 30;

    public static function hashIp(string $ip, string $userAgent): ?string
    {
        if ($ip === '' && $userAgent === '') {
            return null;
        }

        $salt = getenv('IP_HASH_SALT') ?: 'trono_salt';
        $payload = $ip . '|' . $userAgent . '|' . $salt;

        return hash('sha256', $payload);
    }

    public static function ensureNicknameIsSafe(string $nickname): void
    {
        $clean = trim($nickname);
        if ($clean === '' || mb_strlen($clean) > 40) {
            throw new RuntimeException('Escolha um apelido amigável de até 40 caracteres.');
        }

        if (preg_match('/https?:\/\//i', $clean)) {
            throw new RuntimeException('Sem links no apelido, majestade.');
        }

        if (preg_match('/(.)\1{3,}/u', $clean)) {
            throw new RuntimeException('Menos caracteres repetidos no apelido, por favor.');
        }

        if (!preg_match('/^[\p{L}0-9 _\-]{2,40}$/u', $clean)) {
            throw new RuntimeException('Use apenas letras, números, espaços, hífens ou underscores.');
        }
    }

    public static function ensureRateLimit(?string $hash): void
    {
        if ($hash === null) {
            return;
        }

        $storageDir = sys_get_temp_dir() . '/trono-rate';
        if (!is_dir($storageDir)) {
            if (!mkdir($storageDir, 0755, true) && !is_dir($storageDir)) {
                throw new RuntimeException('Não foi possível preparar o salão do rate limit.');
            }
        }

        $file = $storageDir . '/' . $hash . '.lock';
        $now = new DateTimeImmutable();

        if (file_exists($file)) {
            $last = (int) file_get_contents($file);
            if ($now->getTimestamp() - $last < self::RATE_LIMIT_SECONDS) {
                throw new RuntimeException('Trono ocupado! Aguarde 30 segundos antes de uma nova coroação.');
            }
        }

        file_put_contents($file, (string) $now->getTimestamp());
    }
}
