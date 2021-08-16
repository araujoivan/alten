package com.alten.cancun.service;

import static com.alten.cancun.constants.ErrorMessage.*;
import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.entity.Reservation;
import com.alten.cancun.feign.ListingRoomFeignClient;
import com.alten.cancun.mapper.ReservationMapper;
import com.alten.cancun.service.rule.IRule;
import com.alten.cancun.service.rule.RuleFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alten.cancun.repository.BookingRepository;

/**
 *
 * @author Eder_Crespo
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private ListingRoomFeignClient listingRoomFeignClient;

    public void registerReservation(ReservationDto reservationDto, RoomDto room) {

        final Reservation reservation = ReservationMapper.mapToReservation(reservationDto, room);

        if (hasNoRuleViolations(reservationDto)) {

            final List<Reservation> reservations = bookingRepository.findByRoom_Number(room.getNumber());

            if (isRoomAvailable(reservationDto.getPeriod(), reservations)) {

                final String code = generateReservationCode(reservation);

                saveOrUpdateReservation(reservation, code);

                reservationDto.setCode(reservation.getCode());
                reservationDto.getPeriod().setCheckIn(reservation.getCheckIn());
                reservationDto.getPeriod().setCheckOut(reservation.getCheckOut());

            } else {
                reservationDto.addErrorMessage(PERIOD_ALREADY_BOOKED.getMessage());
            }
        }
    }

    @Transactional
    public boolean cancelReservation(String reservationCode) {

        boolean reservationExists = bookingRepository.existsByCode(reservationCode);

        if (reservationExists) {
            bookingRepository.deleteByCode(reservationCode);
        }

        return reservationExists;
    }
    
    
    public ReservationDto getReservation(String reservationCode) {
                
        final Reservation reservation = bookingRepository.findByCode(reservationCode);
        
        if (Objects.nonNull(reservation)) {
            return ReservationMapper.mapToReservationDto(reservation);
        } 
        
        final ReservationDto reservationDto = ReservationDto.builder().code(reservationCode).build();
        
        reservationDto.addErrorMessage(RESERVATION_NOT_FOUND.getMessage());
        
        return reservationDto;
    }

    public void modifyReservation(ReservationDto reservationDto) {

        final Reservation reservation = bookingRepository.findByCode(reservationDto.getCode());

        if (Objects.isNull(reservation)) {
            reservationDto.addErrorMessage(RESERVATION_NOT_FOUND.getMessage());
            return;
        }

        if (hasNoRuleViolations(reservationDto)) {

            final List<Reservation> reservations = reservation.getRoom()
                    .getReservations().stream().filter(r -> !r.getCode().equals(reservation.getCode()))
                    .collect(Collectors.toList());
            
            if (isRoomAvailable(reservationDto.getPeriod(), reservations)) {

                reservation.setCheckIn(reservationDto.getPeriod().getCheckIn());
                reservation.setCheckOut(reservationDto.getPeriod().getCheckOut());

                saveOrUpdateReservation(reservation, reservation.getCode());

                reservationDto.getPeriod().setCheckIn(reservation.getCheckIn());
                reservationDto.getPeriod().setCheckOut(reservation.getCheckOut());

            } else {
                reservationDto.addErrorMessage(PERIOD_ALREADY_BOOKED.getMessage());
            }
        }
    }

    private boolean hasNoRuleViolations(ReservationDto reservationDto) {

        final IRule reservationRule = RuleFactory.getReservationRule();

        reservationRule.apply(reservationDto);

        return !reservationDto.hasErrorMessage();
    }

    private boolean isRoomAvailable(PeriodDto period, List<Reservation> reservations) {

        boolean isAvaliable = true;

        if (!reservations.isEmpty()) {

            final Predicate<Reservation> isBusy = r -> {

                boolean busy = (period.getCheckIn().toLocalDate().isEqual(r.getCheckOut().toLocalDate())
                        || period.getCheckIn().toLocalDate().isBefore(r.getCheckOut().toLocalDate()))
                        && (period.getCheckOut().toLocalDate().isEqual(r.getCheckIn().toLocalDate())
                        || period.getCheckOut().toLocalDate().isAfter(r.getCheckIn().toLocalDate()));

                return busy;
            };

            isAvaliable = reservations.stream().noneMatch(isBusy);
        }

        return isAvaliable;
    }

    private void saveOrUpdateReservation(Reservation reservation, String code) {

        reservation.setCode(code);

        adjustCheckinAndCheckoutDates(reservation);

        bookingRepository.save(reservation);
    }

    private void adjustCheckinAndCheckoutDates(Reservation reservation) {

        final LocalDateTime checkIn = LocalDateTime.of(reservation.getCheckIn().toLocalDate(), LocalTime.MIN);
        final LocalDateTime checkOut = LocalDateTime.of(reservation.getCheckOut().toLocalDate(), LocalTime.MAX).minusSeconds(1);

        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
    }

    private String generateReservationCode(Reservation reservation) {
        final UUID reservationCode = UUID.randomUUID();
        return reservationCode.toString().split("-")[0];
    }

    public RoomDto getRoomByNumber(int roomNumber) {
     
        return listingRoomFeignClient.getRoomByNumber(roomNumber).getBody();     
    }
}
