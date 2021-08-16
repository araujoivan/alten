package com.alten.cancun.feign;

import com.alten.cancun.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Eder_Crespo
 */
@Component
@FeignClient(name = "listing-api", url = "localhost:8083", path = "/room")
public interface ListingRoomFeignClient {
    
    @GetMapping(value = "/{roomNumber}",  produces = "application/json")
    public ResponseEntity<RoomDto> getRoomByNumber(@PathVariable Integer roomNumber);
    
}
