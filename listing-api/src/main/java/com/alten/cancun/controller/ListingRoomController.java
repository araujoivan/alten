package com.alten.cancun.controller;

import com.alten.cancun.dto.PeriodDto;
import com.alten.cancun.dto.RoomUnavailabilityDto;
import com.alten.cancun.dto.RoomDto;
import com.alten.cancun.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Eder_Crespo
 */
@Api(value = "Listing Room")
@RestController
@RequestMapping(value = "/room")
public class ListingRoomController {

    @Autowired
    private RoomService roomService;

    @ApiOperation(value = "Get available rooms")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns a list of available rooms in a period")})
    @GetMapping(value = "/period", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<RoomUnavailabilityDto>> listAvailableRooms(@RequestBody PeriodDto period) {

        final List<RoomUnavailabilityDto> roomAvailableList = roomService.listAvailableRooms(period);

        return ResponseEntity.ok(roomAvailableList);
    }
    
    @ApiOperation(value = "Get room by number")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns a room according to the number passed as parameter")})
    @GetMapping(value = "/{roomNumber}",  produces = "application/json")
    public ResponseEntity<RoomDto> getRoomByNumber(@PathVariable Integer roomNumber) {

        final RoomDto roomDto = roomService.getRoomByNumber(roomNumber);

        return ResponseEntity.ok(roomDto);
    }
    
    @ApiOperation(value = "Get all rooms")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns all rooms")})
    @GetMapping(value = "/all",  produces = "application/json")
    public ResponseEntity<List<RoomDto>> getAllRooms() {

        final List<RoomDto> roomDtoList = roomService.getAllRooms();

        return ResponseEntity.ok(roomDtoList);
    }
}
