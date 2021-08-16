package com.alten.cancun.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Eder_Crespo
 */

@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class PeriodDto { 
    
    @ApiModelProperty(value = "Check in date")
    private LocalDateTime checkIn;
    
    @ApiModelProperty(value = "Check out date")
    private LocalDateTime checkOut;
    
}
