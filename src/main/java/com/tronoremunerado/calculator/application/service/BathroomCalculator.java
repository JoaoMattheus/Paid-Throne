package com.tronoremunerado.calculator.application.service;

import org.springframework.stereotype.Component;

@Component
public class BathroomCalculator {
    public double calculateDailyPercentageOfShift(int averageBathroomTime, int numberOfVisitsPerDay, int minutesWorked) {
        return (averageBathroomTime * numberOfVisitsPerDay * 100) / (double) minutesWorked;
    }
}
