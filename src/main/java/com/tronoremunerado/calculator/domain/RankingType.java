package com.tronoremunerado.calculator.domain;

import lombok.Getter;

@Getter
public enum RankingType {
    HIGHER_EARNINGS(" order by daily_earnings desc, username asc limit 5;"),
    HIGHER_MINUTES(" order by daily_minutes_spent desc, username asc limit 5;"),
    LOWER_MINUTES(" order by daily_minutes_spent asc, username asc limit 5;");

    private final String queryOrder;

    RankingType(String queryOrder) {
        this.queryOrder = queryOrder;
    }
}
