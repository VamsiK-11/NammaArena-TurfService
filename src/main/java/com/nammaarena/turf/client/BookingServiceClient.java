package com.nammaarena.turf.client;

import com.nammaarena.turf.dto.BookingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "booking-service")
public interface BookingServiceClient {
    
    @GetMapping("/api/bookings/turf/{turfId}")
    List<BookingDto> getBookingsByTurfId(@PathVariable Long turfId);
}