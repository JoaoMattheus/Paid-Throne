<?php

namespace App\Infra;

use App\Config\DB;
use App\Domain\Scales;
use PDO;

class SubmissionRepo
{
    private PDO $pdo;

    public function __construct()
    {
        $this->pdo = DB::connection();
    }

    /**
     * @param array<string, mixed> $payload
     */
    public function store(array $payload): int
    {
        $sql = 'INSERT INTO submissions (
            nickname, scale_code, minutes_per_day, days_per_week, hours_per_day,
            days_per_month, work_hours_per_month, throne_minutes_month, throne_minutes_year,
            throne_money_month, throne_money_year, scale_version, ip_hash, user_agent
        ) VALUES (
            :nickname, :scale_code, :minutes_per_day, :days_per_week, :hours_per_day,
            :days_per_month, :work_hours_per_month, :throne_minutes_month, :throne_minutes_year,
            :throne_money_month, :throne_money_year, :scale_version, :ip_hash, :user_agent
        )';

        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            'nickname' => $payload['nickname'],
            'scale_code' => $payload['scale_code'],
            'minutes_per_day' => $payload['minutes_per_day'],
            'days_per_week' => $payload['days_per_week'],
            'hours_per_day' => $payload['hours_per_day'],
            'days_per_month' => $payload['days_per_month'],
            'work_hours_per_month' => $payload['work_hours_per_month'],
            'throne_minutes_month' => $payload['throne_minutes_month'],
            'throne_minutes_year' => $payload['throne_minutes_year'],
            'throne_money_month' => $payload['throne_money_month'],
            'throne_money_year' => $payload['throne_money_year'],
            'scale_version' => Scales::SCALE_VERSION,
            'ip_hash' => $payload['ip_hash'] ?? null,
            'user_agent' => $payload['user_agent'] ?? null,
        ]);

        return (int) $this->pdo->lastInsertId();
    }

    /**
     * @return array<string, mixed>|null
     */
    public function find(int $id): ?array
    {
        $stmt = $this->pdo->prepare('SELECT * FROM submissions WHERE id = :id');
        $stmt->execute(['id' => $id]);
        $result = $stmt->fetch();

        return $result === false ? null : $result;
    }

    /**
     * @return array<int, array<string, mixed>>
     */
    public function topTimeByScale(string $scaleCode, int $limit = 5): array
    {
        return $this->fetchLeaderboard('throne_minutes_month', 'DESC', $scaleCode, $limit);
    }

    public function topMoneyByScale(string $scaleCode, int $limit = 5): array
    {
        return $this->fetchLeaderboard('throne_money_month', 'DESC', $scaleCode, $limit);
    }

    public function shortReignsByScale(string $scaleCode, int $limit = 5): array
    {
        return $this->fetchLeaderboard('throne_minutes_month', 'ASC', $scaleCode, $limit);
    }

    /**
     * @return array<int, array<string, mixed>>
     */
    public function topTimeGlobal(int $limit = 5): array
    {
        return $this->fetchLeaderboard('throne_minutes_month', 'DESC', null, $limit);
    }

    public function topMoneyGlobal(int $limit = 5): array
    {
        return $this->fetchLeaderboard('throne_money_month', 'DESC', null, $limit);
    }

    public function shortReignsGlobal(int $limit = 5): array
    {
        return $this->fetchLeaderboard('throne_minutes_month', 'ASC', null, $limit);
    }

    /**
     * @return array<int, array<string, mixed>>
     */
    private function fetchLeaderboard(string $column, string $direction, ?string $scaleCode, int $limit): array
    {
        $direction = strtoupper($direction) === 'ASC' ? 'ASC' : 'DESC';
        $sql = 'SELECT nickname, scale_code, ' . $column . ' FROM submissions';
        $params = [];
        if ($scaleCode !== null) {
            $sql .= ' WHERE scale_code = :scale_code';
            $params['scale_code'] = $scaleCode;
        }
        $sql .= ' ORDER BY ' . $column . ' ' . $direction . ' LIMIT ' . (int) $limit;

        $stmt = $this->pdo->prepare($sql);
        $stmt->execute($params);

        return $stmt->fetchAll();
    }
}
