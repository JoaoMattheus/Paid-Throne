package com.tronoremunerado.calculator.service;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.ShiftTime;
import org.springframework.stereotype.Service;

@Service
public class CalculatorServiceImpl implements CalculatorService{
    @Override
    public KingCalculateResponse calculate(King king) {
        return new KingCalculateResponse(
                king.username(),
                king.getMinutesSpent(ShiftTime.DAILY),
                king.getMinutesSpent(ShiftTime.MONTHLY),
                king.getMinutesSpent(ShiftTime.YEARLY),
                king.getEarningsPerMinute(ShiftTime.DAILY),
                king.getEarningsPerMinute(ShiftTime.MONTHLY),
                king.getEarningsPerMinute(ShiftTime.YEARLY),
                king.salary()
        );
    }




}
