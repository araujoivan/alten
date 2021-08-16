package com.alten.cancun.service.rule;

import com.alten.cancun.service.rule.period.InitialReservationDayRule;
import com.alten.cancun.service.rule.period.BookingInAdvancedRule;
import com.alten.cancun.service.rule.period.MissingReservationPeriodRule;
import com.alten.cancun.service.rule.period.LmitOfReservationDaysRule;

/**
 *
 * @author Eder_Crespo
 */
public class RuleFactory {

    public static IRule getReservationRule() {

        final IRule reservationRule =
                new MissingReservationPeriodRule(
                    new BookingInAdvancedRule(
                        new InitialReservationDayRule(
                            new LmitOfReservationDaysRule(r -> {}))));
        
        return reservationRule;
    }
}
