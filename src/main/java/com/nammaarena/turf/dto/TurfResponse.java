package com.nammaarena.turf.dto;

import com.nammaarena.turf.enums.TurfStatus;
import lombok.Data;
import java.util.List;

@Data
public class TurfResponse {
    private Long id;
    private String name;
    private String location;
    private String imageUrl;
    private String startTime;
    private String endTime;
    private String availableTimings;
    private Long ownerId;
    private TurfStatus status;
    private List<SportDto> sports;
}