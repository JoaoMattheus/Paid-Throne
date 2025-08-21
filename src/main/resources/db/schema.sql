DROP TABLE IF EXISTS king
CREATE TABLE IF NOT EXISTS king (
    id VARCHAR(36) PRIMARY KEY,

    username VARCHAR(15) NOT NULL
        CHECK (LENGTH(username) BETWEEN 5 AND 15)
        CHECK (username ~ '^[a-zA-Z0-9]+$'),

    daily_minutes_spent SMALLINT NOT NULL,
    monthly_minutes_spent SMALLINT NOT NULL,
    yearly_minutes_spent SMALLINT NOT NULL,

    daily_earnings NUMERIC(10,2) NOT NULL CHECK (daily_earnings <= 50000),
    monthly_earnings NUMERIC(10,2) NOT NULL CHECK (monthly_earnings <= 50000),
    yearly_earnings NUMERIC(10,2) NOT NULL CHECK (yearly_earnings <= 50000),

    daily_percentage_of_shift NUMERIC(5,2) NOT NULL
        CHECK (daily_percentage_of_shift >= 0 AND daily_percentage_of_shift <= 100),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);