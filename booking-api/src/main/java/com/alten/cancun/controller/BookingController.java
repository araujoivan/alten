package com.alten.cancun.controller;

import com.alten.cancun.dto.ReservationDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eder_Crespo
 */
@Api(value = "Bookings")
@RestController
@RequestMapping(value = "/booking")
public class BookingController {

    private static Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @ApiOperation(value = "Book a reservation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns a registred reservation")})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReservationDto> registerReservation(@RequestBody ReservationDto reservationDto) {

        final RoomDto room = bookingService.getRoomByNumber(reservationDto.getRoomNumber());
        
        if(Objects.isNull(room.getNumber())) {
            logger.info("Room does not exist!");
            throw new RuntimeException("Room does not exist!");
        }

        bookingService.registerReservation(reservationDto, room);

        return ResponseEntity.ok(reservationDto);
    }

    @ApiOperation(value = "Modify checkin and checkout dates from an existent reservation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns a modified reservation")})
    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReservationDto> modifyReservation(@RequestBody ReservationDto reservationDto) {

        bookingService.modifyReservation(reservationDto);

        return ResponseEntity.ok(reservationDto);
    }

    @ApiOperation(value = "Cancel a reservation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the status of the cancelation")})
    @DeleteMapping("/{reservationCode}")
    public ResponseEntity<String> cancelReservation(@PathVariable String reservationCode) {

        final boolean isSuccess = bookingService.cancelReservation(reservationCode);

        return isSuccess ? ResponseEntity.ok("Reservation canceled successfully!") : ResponseEntity.notFound().build();
    }
    
    @ApiOperation(value = "Get a reservation by code")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns a reservation")})
    @GetMapping(value = "/{reservationCode}", produces = "application/json")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable String reservationCode) {

        final ReservationDto reservation = bookingService.getReservation(reservationCode);

        return ResponseEntity.ok(reservation);
    }
}
