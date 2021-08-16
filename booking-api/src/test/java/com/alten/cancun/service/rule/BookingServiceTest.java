package com.alten.cancun.service.rule;

import static com.alten.cancun.constants.ErrorMessage.RESERVATION_NOT_FOUND;
import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.entity.Reservation;
import com.alten.cancun.entity.Room;
import com.alten.cancun.feign.ListingRoomFeignClient;
import com.alten.cancun.service.BookingService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import com.alten.cancun.repository.BookingRepository;

/**
 *
 * @author Eder_Crespo
 */

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private ListingRoomFeignClient listingRoomFeignClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    
    @Test
    public void should_save_reservation_successfully() {
        
        final LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        final LocalDateTime checkOut = checkIn.plusDays(1);
        final int roomNumber = 123;
        
        // Prepare
        final ReservationDto reservationDto = ReservationDto.builder()
                .clienId(1)
                .period(PeriodDto.builder()
                        .checkIn(checkIn)
                        .checkOut(checkOut)
                        .build())
                .roomNumber(roomNumber)
                .build();
        
        final RoomDto room = RoomDto.builder()
                .reservations(Collections.EMPTY_LIST)
                .number(roomNumber).build();
        
        when(bookingRepository.save(any(Reservation.class))).thenReturn(any(Reservation.class));
        
        // Execute
        bookingService.registerReservation(reservationDto, room);
        
        // Assert
        Assert.assertEquals(checkIn.toLocalDate(), reservationDto.getPeriod().getCheckIn().toLocalDate());
        Assert.assertEquals(checkOut.toLocalDate(), reservationDto.getPeriod().getCheckOut().toLocalDate());
    }

    @Test
    public void should_delete_reservation_successfully_when_exist() {

        // Prepare
        final String expectedCode = "abc";

        when(bookingRepository.existsByCode(expectedCode)).thenReturn(true);

        // Execute
        bookingService.cancelReservation(expectedCode);

        // Assert
        verify(bookingRepository, times(1)).deleteByCode(expectedCode);
    }
    
    @Test
    public void should_not_delete_reservation_when_reservation_not_found() {

        // Prepare
        final String expectedCode = "def";

        when(bookingRepository.existsByCode(expectedCode)).thenReturn(false);

        // Execute
        bookingService.cancelReservation(expectedCode);

        // Assert
        verify(bookingRepository, never()).deleteByCode(expectedCode);
    }
    
    @Test
    public void should_get_reservation_by_code_successfully() {

        // Prepare
        final String expectedCode = "ghi";
        
        final Reservation reservation = createReservation(expectedCode);

        when(bookingRepository.findByCode(expectedCode)).thenReturn(reservation);

        // Execute
        final ReservationDto reservationDto = bookingService.getReservation(expectedCode);

         // Assert
        Assert.assertEquals(expectedCode, reservationDto.getCode());
    }

    @Test
    public void should_not_found_reservation_by_code() {

        // Prepare
        final String expectedCode = "jkl";
        
        final Reservation reservation = createReservation(expectedCode);

        when(bookingRepository.findByCode(expectedCode)).thenReturn(null);

        // Execute
        final ReservationDto reservationDto = bookingService.getReservation(expectedCode);

         // Assert
        Assert.assertFalse(reservationDto.getErrorMesssages().isEmpty());
        Assert.assertTrue(reservationDto.getErrorMesssages().contains(RESERVATION_NOT_FOUND.getMessage()));
    }
    
    private Reservation createReservation(String code) {
        return Reservation.builder()
                .room(Room.builder().build())
                .code(code)
                .build();
    }

}
