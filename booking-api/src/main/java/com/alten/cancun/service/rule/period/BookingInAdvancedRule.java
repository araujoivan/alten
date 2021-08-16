package com.alten.cancun.service.rule.period;

import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.service.rule.RuleDecorator;
import static com.alten.cancun.constants.ErrorMessage.LIMIT_OF_DAYS_IN_ADVANCED_WHEN_BOOKING;
import com.alten.cancun.service.rule.IRule;
import java.time.LocalDate;

/**
 *
 * @author Eder_Crespo
 */

// This rule is used to set the days in advanced for booking
public class BookingInAdvancedRule extends RuleDecorator {
    
    public BookingInAdvancedRule(IRule rule) {
        super(rule);
    }

    @Override
    public void apply(ReservationDto reservation) {
        
        final LocalDate thirtyDaysAway = LocalDate.now().plusDays(30);
        
        final LocalDate checkIn = reservation.getPeriod().getCheckIn().toLocalDate();

        if(checkIn.isAfter(thirtyDaysAway)) {
            reservation.addErrorMessage(LIMIT_OF_DAYS_IN_ADVANCED_WHEN_BOOKING.getMessage());
        }
        
        rule.apply(reservation);
    }
}
