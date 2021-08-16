package com.alten.cancun.service;

import static com.alten.cancun.constants.ErrorMessage.ROOM_NOT_FOUND;
import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.RoomUnavailabilityDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.entity.Reservation;
import com.alten.cancun.entity.Room;
import com.alten.cancun.mapper.ListingRoomMapper;
import com.alten.cancun.repository.RoomRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Eder_Crespo
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<RoomUnavailabilityDto> listAvailableRooms(PeriodDto period) {

        final List<RoomUnavailabilityDto> availableRooms = new ArrayList();

        final Comparator<Reservation> byCheckinDate = (r1, r2) -> r1.getCheckIn().compareTo(r2.getCheckIn());

        final Predicate<Reservation> filterBetweenPeriods = getFilterBetweenPeriods(period);

        final List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {

            final List<Reservation> reservations = room.getReservations();

            if (!reservations.isEmpty()) {

                final List<PeriodDto> periods = reservations.stream()
                        .sorted(byCheckinDate)
                        .filter(filterBetweenPeriods)
                        .map(this::mapToPeriodDto).collect(Collectors.toList());

                final RoomUnavailabilityDto availableRoom = RoomUnavailabilityDto.builder()
                        .unavailablePeriods(periods)
                        .room(RoomDto.builder()
                                .number(room.getNumber())
                                .build())
                        .build();

                availableRooms.add(availableRoom);
            }
        }

        return availableRooms;
    }

    private PeriodDto mapToPeriodDto(Reservation reservation) {
        return PeriodDto.builder()
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut()).build();
    }

    private Predicate<Reservation> getFilterBetweenPeriods(PeriodDto period) {

        final Predicate<Reservation> filterBetweenPeriod = reservation -> {

            final LocalDate checkIn = reservation.getCheckIn().toLocalDate();
            final LocalDate checkOut = reservation.getCheckOut().toLocalDate();

            boolean isBetweenDates = (period.getCheckIn().toLocalDate().isBefore(checkIn) || period.getCheckIn().toLocalDate().equals(checkIn))
                    && (period.getCheckOut().toLocalDate().isAfter(checkOut) || period.getCheckOut().toLocalDate().equals(checkOut));

            return isBetweenDates;
        };

        return filterBetweenPeriod;
    }

    public RoomDto getRoomByNumber(Integer number) {

        final Room room = roomRepository.findByNumber(number);

        if (Objects.isNull(room)) {
            throw new RuntimeException(ROOM_NOT_FOUND.getMessage());
        }

        return ListingRoomMapper.mapToRoomDto(room);
    }

    public List<RoomDto> getAllRooms() {

        final List<Room> roomList = roomRepository.findAll();

        return ListingRoomMapper.mapToRoomDtoList(roomList);
    }
}
