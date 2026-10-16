package com.nammaarena.turf.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private Long userId;
    private Long turfId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalAmount;
    private String status;
}