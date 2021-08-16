package com.alten.cancun.service.rule.period;

import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.service.rule.RuleDecorator;
import java.time.temporal.ChronoUnit;
import static com.alten.cancun.constants.ErrorMessage.LIMIT_OF_DAYS_FOR_RESERVATION;
import com.alten.cancun.service.rule.IRule;
import java.time.LocalDate;

/**
 *
 * @author Eder_Crespo
 */

// This rule is used to set the maximum amount of booking days 
public class LmitOfReservationDaysRule extends RuleDecorator {
    
    private final static int LIMIT_OF_RESERVATION_DAYS_ALLOWED = 3;
    
    public LmitOfReservationDaysRule(IRule rule) {
        super(rule);
    }

    @Override
    public void apply(ReservationDto reservation) {
        
        final LocalDate checkIn = reservation.getPeriod().getCheckIn().toLocalDate();
        final LocalDate checkOut = reservation.getPeriod().getCheckOut().toLocalDate();
        
        long daysBetween = checkIn.until(checkOut, ChronoUnit.DAYS);
        
        if(daysBetween >= LIMIT_OF_RESERVATION_DAYS_ALLOWED) {
            reservation.addErrorMessage(LIMIT_OF_DAYS_FOR_RESERVATION.getMessage());
        } 
        
        rule.apply(reservation);
    }
}
