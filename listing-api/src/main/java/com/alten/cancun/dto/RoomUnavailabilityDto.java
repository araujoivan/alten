package com.alten.cancun.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Eder_Crespo
 */

@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class RoomUnavailabilityDto {
    
    private RoomDto room;
       
    private List<PeriodDto> unavailablePeriods = new ArrayList();
}
