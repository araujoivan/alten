package com.alten.cancun.service.rule.period;

import static com.alten.cancun.constants.ErrorMessage.*;
import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.service.rule.IRule;
import com.alten.cancun.service.rule.RuleDecorator;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Eder_Crespo
 */
// This rule is used just to perform an initial validation of existence of booking period
public class MissingReservationPeriodRule extends RuleDecorator {

    public MissingReservationPeriodRule(IRule rule) {
        super(rule);
    }

    @Override
    public void apply(ReservationDto reservation) {

        final PeriodDto period = reservation.getPeriod();

        if (Objects.isNull(period)) {

            reservation.addErrorMessage(PERIOD_NOT_DEFINED.getMessage());

        } else {

            final LocalDateTime checkIn = period.getCheckIn();
            final LocalDateTime checkOut = period.getCheckOut();

            if (Objects.isNull(checkIn) || Objects.isNull(checkOut)) {
                reservation.addErrorMessage(CHECK_IN_OR_CHECK_OUT_NOT_DEFINED.getMessage());
            } else if (checkOut.toLocalDate().isBefore(checkIn.toLocalDate())) {
                reservation.addErrorMessage(CHECK_OUT_BEFORE_CHECK_IN.getMessage());
            }
        }

        // I can't go ahead if one of these initial validations fails
        if (!reservation.hasErrorMessage()) {
            rule.apply(reservation);
        }
    }
}
