package com.alten.cancun.service.rule.period;

import com.alten.cancun.service.rule.period.LmitOfReservationDaysRule;
import static com.alten.cancun.constants.ErrorMessage.LIMIT_OF_DAYS_FOR_RESERVATION;
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
public class LimitOfReservationDaysRuleTest {

    @Test
    public void should_success_when_booking_with_an_interval_of_up_to_3_days() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now();
        final LocalDateTime checkOut = checkIn.plusDays(2);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule limitOfReservationDaysRule = new LmitOfReservationDaysRule(r -> {});

        // Execute
        limitOfReservationDaysRule.apply(reservation);

        // Assert
        Assert.assertTrue(reservation.getErrorMesssages().isEmpty());
    }
    
    @Test
    public void should_fail_when_booking_with_an_interval_of_more_than_3_days() {

        // Prepare
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime checkIn = LocalDateTime.now();
        final LocalDateTime checkOut = checkIn.plusDays(3);

        final ReservationDto reservation = createReservation(checkIn, checkOut);

        final IRule limitOfReservationDaysRule = new LmitOfReservationDaysRule(r -> {});

        // Execute
        limitOfReservationDaysRule.apply(reservation);

        // Assert
        Assert.assertFalse(reservation.getErrorMesssages().isEmpty());
        Assert.assertEquals(LIMIT_OF_DAYS_FOR_RESERVATION.getMessage(), reservation.getErrorMesssages().get(0));
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
