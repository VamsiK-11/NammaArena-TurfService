package com.nammaarena.turf.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class TurfRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private String imageUrl;
    
    @NotBlank(message = "Start time is required")
    private String startTime;
    
    @NotBlank(message = "End time is required")
    private String endTime;
    
    @NotEmpty(message = "At least one sport is required")
    @Valid
    private List<SportDto> sports;
}