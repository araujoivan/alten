package com.alten.cancun.service.rule.period;

import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.service.rule.RuleDecorator;
import static com.alten.cancun.constants.ErrorMessage.START_DAY_AFTER_BOOKING;
import com.alten.cancun.service.rule.IRule;
import java.time.LocalDate;

/**
 *
 * @author Eder_Crespo
 */

// This rule is used to set the appropriate day to start the booking period.
public class InitialReservationDayRule extends RuleDecorator {
    
    public InitialReservationDayRule(IRule rule) {
        super(rule);
    }

    @Override
    public void apply(ReservationDto reservation) {
        
        final LocalDate oneDayAfterBooking = LocalDate.now().plusDays(1);
        
        final LocalDate checkIn = reservation.getPeriod().getCheckIn().toLocalDate();

        if(checkIn.isBefore(oneDayAfterBooking)) {
            reservation.addErrorMessage(START_DAY_AFTER_BOOKING.getMessage());
        }
        
        rule.apply(reservation);
    }
}
