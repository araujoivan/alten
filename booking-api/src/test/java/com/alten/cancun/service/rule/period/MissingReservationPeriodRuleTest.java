package com.alten.cancun.service.rule.period;

import com.alten.cancun.service.rule.period.MissingReservationPeriodRule;
import static com.alten.cancun.constants.ErrorMessage.*;
import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.service.rule.IRule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Eder_Crespo
 */
public class MissingReservationPeriodRuleTest {

    @Test
    public void should_fail_when_checkout_is_missing() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        final LocalDateTime checkOut = null;

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule missingReservationPeriodRule = new MissingReservationPeriodRule(r -> {});

        // Execute
        missingReservationPeriodRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(CHECK_IN_OR_CHECK_OUT_NOT_DEFINED.getMessage(), reservation.getErrorMesssages().get(0));
    }
    
    @Test
    public void should_fail_when_checkin_is_missing() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = null;
        final LocalDateTime checkOut = LocalDateTime.now().plusDays(1);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule missingReservationPeriodRule = new MissingReservationPeriodRule(r -> {});

        // Execute
        missingReservationPeriodRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(CHECK_IN_OR_CHECK_OUT_NOT_DEFINED.getMessage(), reservation.getErrorMesssages().get(0));
    }
    
    @Test
    public void should_fail_when_checkin_is_greater_than_checkout() {

        // Prepare
        final LocalDateTime checkOut = LocalDateTime.now();
        final LocalDateTime checkIn = checkOut.plusDays(1);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule missingReservationPeriodRule = new MissingReservationPeriodRule(r -> {});

        // Execute
        missingReservationPeriodRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(CHECK_OUT_BEFORE_CHECK_IN.getMessage(), reservation.getErrorMesssages().get(0));
    }
    
    @Test
    public void should_fail_when_period_is_missing() {

        // Prepare
        final ReservationDto reservation = ReservationDto.builder()
                .errorMesssages(new ArrayList<String>())
                .build();

        final IRule missingReservationPeriodRule = new MissingReservationPeriodRule(r -> {});

        // Execute
        missingReservationPeriodRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(PERIOD_NOT_DEFINED.getMessage(), reservation.getErrorMesssages().get(0));
    }

    private ReservationDto createReservation(LocalDateTime checkIn, LocalDateTime checkOut) {
        return ReservationDto.builder()
                .errorMesssages(new ArrayList<String>())
                .period(PeriodDto.builder()
                        .checkIn(checkIn)
                        .checkOut(checkOut)
                        .build())
                .build();
    }

}
