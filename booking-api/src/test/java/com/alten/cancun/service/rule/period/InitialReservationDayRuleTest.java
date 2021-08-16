package com.alten.cancun.service.rule.period;

import com.alten.cancun.service.rule.period.InitialReservationDayRule;
import static com.alten.cancun.constants.ErrorMessage.START_DAY_AFTER_BOOKING;
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
public class InitialReservationDayRuleTest {

    @Test
    public void should_success_when_reservation_starts_one_day_after_booking_today() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = now.plusDays(1);
        final LocalDateTime checkOut = checkIn.plusDays(2);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule initialReservationDayRule = new InitialReservationDayRule(r -> {});

        // Execute
        initialReservationDayRule.apply(reservation);

        // Assert
        Assert.assertTrue(reservation.getErrorMesssages().isEmpty());
    }
    
    @Test
    public void should_fail_when_reservation_starts_at_same_day_as_booking_day() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = now;
        final LocalDateTime checkOut = checkIn.plusDays(2);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule initialReservationDayRule = new InitialReservationDayRule(r -> {});

        // Execute
        initialReservationDayRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(START_DAY_AFTER_BOOKING.getMessage(), reservation.getErrorMesssages().get(0));
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
