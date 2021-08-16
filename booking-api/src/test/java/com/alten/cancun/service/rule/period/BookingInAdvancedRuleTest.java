package com.alten.cancun.service.rule.period;

import com.alten.cancun.service.rule.period.BookingInAdvancedRule;
import static com.alten.cancun.constants.ErrorMessage.LIMIT_OF_DAYS_IN_ADVANCED_WHEN_BOOKING;
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
public class BookingInAdvancedRuleTest {

    @Test
    public void should_success_when_booking_with_up_to_30_days_advance() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now().plusDays(30);
        final LocalDateTime checkOut = checkIn.plusDays(1);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule bookingInAdvanceRule = new BookingInAdvancedRule(r -> {});

        // Execute
        bookingInAdvanceRule.apply(reservation);

        // Assert
        Assert.assertTrue(reservation.getErrorMesssages().isEmpty());
    }
    
    @Test
    public void should_fail_when_booking_with_more_then_30_days_advance() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now().plusDays(31);
        final LocalDateTime checkOut = checkIn.plusDays(1);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule bookingInAdvanceRule = new BookingInAdvancedRule(r -> {});

        // Execute
        bookingInAdvanceRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(LIMIT_OF_DAYS_IN_ADVANCED_WHEN_BOOKING.getMessage(), reservation.getErrorMesssages().get(0));
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
