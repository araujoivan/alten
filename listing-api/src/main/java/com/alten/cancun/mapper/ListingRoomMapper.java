package com.alten.cancun.mapper;

import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.dto.RoomUnavailabilityDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.entity.Reservation;
import com.alten.cancun.entity.Room;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Eder_Crespo
 */
public class ListingRoomMapper {

    public static List<RoomDto> mapToRoomDtoList(List<Room> rooms) {
        return rooms.stream().map(ListingRoomMapper::mapToRoomDto).collect(Collectors.toList());
    }

    public static RoomDto mapToRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .build();
    }

    public static Room mapToRoom(RoomDto room) {
        return Room.builder()
                .number(room.getNumber())
                .build();
    }

    public static RoomUnavailabilityDto mapToRoomAvailabilityDto(Room room) {
        return RoomUnavailabilityDto.builder()
                .build();
    }

    public static List<ReservationDto> mapToReservationDtoList(List<Reservation> reservations) {
        return reservations.stream().map(ListingRoomMapper::mapToReservationDto)
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
}
