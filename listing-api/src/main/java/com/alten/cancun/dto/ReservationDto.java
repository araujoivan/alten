package com.alten.cancun.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@Builder
@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ReservationDto {
    
    private long clienId;
    
    private int roomNumber;
    
    private PeriodDto period;
    
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
