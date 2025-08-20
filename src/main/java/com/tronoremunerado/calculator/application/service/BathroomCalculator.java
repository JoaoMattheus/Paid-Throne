package com.tronoremunerado.calculator.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BathroomCalculator {
    public double calculateDailyPercentageOfShift(int averageBathroomTime, int numberOfVisitsPerDay, int minutesWorked) {
        log.info("Calculating daily percentage of shift for: averageBathroomTime={}, numberOfVisitsPerDay={}, minutesWorked={}",
                 averageBathroomTime, numberOfVisitsPerDay, minutesWorked);

        double percentage = (averageBathroomTime * numberOfVisitsPerDay * 100) / (double) minutesWorked;
        
        log.info("Daily percentage of shift calculated: {}%", percentage);
        return percentage;
    }
}
