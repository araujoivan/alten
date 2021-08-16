package com.alten.cancun.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

/**
 *
 * @author Eder_Crespo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
public class ReservationDto {
    
    @ApiModelProperty(value = "Client code")
    private long clienId;
    
    @ApiModelProperty(value = "Room number")
    private int roomNumber;
    
    @ApiModelProperty(value = "Period of availability")
    private PeriodDto period;
    
    @ApiModelProperty(value = "A code generated after a success reservation")
    private String code;
    
    @Default
    private List<String> errorMesssages = new ArrayList();;
    
    public ReservationDto() {
        this.errorMesssages = new ArrayList();
    }

    public void addErrorMessage(String message) {
        errorMesssages.add(message);
    } 

    public boolean hasErrorMessage() {
        return !errorMesssages.isEmpty();
    }
}
