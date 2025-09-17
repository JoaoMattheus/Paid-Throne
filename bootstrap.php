<?php

declare(strict_types=1);

const BASE_PATH = __DIR__;

if (!function_exists('str_starts_with')) {
    function str_starts_with(string $haystack, string $needle): bool
    {
        return $needle === '' || strncmp($haystack, $needle, strlen($needle)) === 0;
    }
}

if (!function_exists('str_contains')) {
    function str_contains(string $haystack, string $needle): bool
    {
        return $needle === '' || strpos($haystack, $needle) !== false;
    }
}

spl_autoload_register(function (string $class): void {
    $prefix = 'App\\';
    $class = ltrim($class, '\\');
    if (!str_starts_with($class, $prefix)) {
        return;
    }

    $relativeClass = substr($class, strlen($prefix));
    $file = BASE_PATH . '/app/' . str_replace('\\', '/', $relativeClass) . '.php';
    if (is_file($file)) {
        require_once $file;
    }
});

function loadEnv(string $path): void
{
    if (!is_file($path)) {
        return;
    }

    $lines = file($path, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
    if ($lines === false) {
        return;
    }

    foreach ($lines as $line) {
        $line = trim($line);
        if ($line === '' || str_starts_with($line, '#')) {
            continue;
        }

        if (!str_contains($line, '=')) {
            continue;
        }

        [$name, $value] = explode('=', $line, 2);
        $name = trim($name);
        $value = trim($value);

        if (!array_key_exists($name, $_ENV)) {
            putenv("{$name}={$value}");
        }
        $_ENV[$name] = $value;
        $_SERVER[$name] = $value;
    }
}

if (!function_exists('env')) {
    function env(string $key, mixed $default = null): mixed
    {
        $value = $_ENV[$key] ?? $_SERVER[$key] ?? getenv($key);
        return $value === false || $value === null ? $default : $value;
    }
}

$envFile = BASE_PATH . '/.env';
loadEnv($envFile);

$timezone = env('APP_TIMEZONE', 'America/Sao_Paulo');
if (function_exists('date_default_timezone_set')) {
    date_default_timezone_set($timezone);
}

function asset(string $path): string
{
    $normalized = '/' . ltrim($path, '/');
    $fullPath = BASE_PATH . '/public' . $normalized;
    $version = is_file($fullPath) ? (string) filemtime($fullPath) : '1';
    return $normalized . '?v=' . $version;
}

function format_currency(float $value): string
{
    return 'R$ ' . number_format($value, 2, ',', '.');
}

function format_percentage(float $value): string
{
    return number_format($value, 2, ',', '.') . '%';
}

function sanitize_output(string $value): string
{
    return htmlspecialchars($value, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
}
