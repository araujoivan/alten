package com.alten.cancun.service.rule;

import com.alten.cancun.service.rule.RuleFactory;
import com.alten.cancun.service.rule.IRule;
import static com.alten.cancun.constants.ErrorMessage.*;
import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Eder_Crespo
 */
public class RuleFactoryTest {
    
    @Test
    public void should_fail_when_booking_with_more_then_30_days_advance() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now().plusDays(31);
        final LocalDateTime checkOut = checkIn.plusDays(1);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        IRule rule = RuleFactory.getReservationRule();
        
        //Execute
        rule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(LIMIT_OF_DAYS_IN_ADVANCED_WHEN_BOOKING.getMessage(), reservation.getErrorMesssages().get(0));
    }
    
    @Test
    public void should_fail_when_booking_with_an_interval_of_more_than_3_days() {

        // Prepare
        final LocalDateTime checkIn = LocalDateTime.now();
        final LocalDateTime checkOut = checkIn.plusDays(5);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        IRule rule = RuleFactory.getReservationRule();
        
        //Execute
        rule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertTrue(reservation.getErrorMesssages().contains(LIMIT_OF_DAYS_FOR_RESERVATION.getMessage()));
    }
    
    @Test
    public void should_fail_when_reservation_and_booking_day_is_the_same_day() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = now;
        final LocalDateTime checkOut = checkIn.plusDays(2);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        IRule rule = RuleFactory.getReservationRule();
        
        //Execute
        rule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(START_DAY_AFTER_BOOKING.getMessage(), reservation.getErrorMesssages().get(0));
    }

    @Test
    public void should_fail_when_checkout_is_missing() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        final LocalDateTime checkOut = null;

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        IRule rule = RuleFactory.getReservationRule();
        
        // Execute
        rule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(CHECK_IN_OR_CHECK_OUT_NOT_DEFINED.getMessage(), reservation.getErrorMesssages().get(0));
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
