package com.alten.cancun.service.rule;

import com.alten.cancun.dto.ReservationDto;

/**
 *
 * @author Eder_Crespo
 */
public class RuleDecorator implements IRule {
    
    protected IRule rule;
    
    public RuleDecorator(IRule rule) {
        this.rule = rule;
    }

    @Override
    public void apply(ReservationDto reservation) {}
    
}
