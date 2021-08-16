package com.alten.cancun.mapper;

import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.entity.Reservation;
import com.alten.cancun.entity.Room;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Eder_Crespo
 */
public class ReservationMapper {
    
    public static List<ReservationDto> mapToReservationDtoList(List<Reservation> reservations) {
        return reservations.stream().map(ReservationMapper::mapToReservationDto)
                .collect(Collectors.toList());
    }
    
    public static List<Reservation> mapToReservationList(List<ReservationDto> reservations) {
        return reservations.stream().map(ReservationMapper::mapToReservation)
                .collect(Collectors.toList());
    }

    public static ReservationDto mapToReservationDto(Reservation reservation) {

        return ReservationDto.builder()
                .clienId(reservation.getClientId())
                .roomNumber(reservation.getRoom().getNumber())
                .period(PeriodDto.builder()
                        .checkIn(reservation.getCheckIn())
                        .checkOut(reservation.getCheckOut())
                        .build())
                .code(reservation.getCode())
                .build();
    }

    public static Reservation mapToReservation(ReservationDto reservation) {
        
        final PeriodDto period = reservation.getPeriod();

        return Reservation.builder()
                .checkIn(Objects.nonNull(period) ? period.getCheckIn() : null)
                .checkOut(Objects.nonNull(period) ? period.getCheckOut() : null)
                .clientId(reservation.getClienId())
                .build();
    }
    
    public static Reservation mapToReservation(ReservationDto reservationDto, Room room) {
        
        final Reservation reservation = mapToReservation(reservationDto);
        
        reservation.setRoom(room);

        return reservation;
    }
    
    public static Reservation mapToReservation(ReservationDto reservationDto, RoomDto room) {
        
        final Reservation reservation = mapToReservation(reservationDto);
        
        reservation.setRoom(mapToRoom(room));

        return reservation;
    }

    public static List<RoomDto> mapToRoomDtoList(List<Room> rooms) {
        return rooms.stream().map(ReservationMapper::mapToRoomDto).collect(Collectors.toList());
    }
    
    public static RoomDto mapToRoomDto(Room room) {
        return RoomDto.builder()
                .number(room.getNumber())
                .build();
    }
    
    public static Room mapToRoom(RoomDto room) {
        return Room.builder()
                .number(room.getNumber())
                .id(room.getId())
                .build();
    }
}
