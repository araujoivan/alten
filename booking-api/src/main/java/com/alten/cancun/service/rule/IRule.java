package com.alten.cancun.service.rule;

import com.alten.cancun.dto.ReservationDto;

/**
 *
 * @author Eder_Crespo
 */
public interface IRule {
        
    public void apply(ReservationDto reservation);

}
