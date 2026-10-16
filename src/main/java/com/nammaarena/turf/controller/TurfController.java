package com.nammaarena.turf.controller;

import com.nammaarena.turf.dto.*;
import com.nammaarena.turf.enums.TurfStatus;
import com.nammaarena.turf.service.TurfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turfs")
@RequiredArgsConstructor
public class TurfController {
    
    private final TurfService turfService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<TurfResponse> createTurf(@Valid @RequestBody TurfRequest request) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(turfService.createTurf(request, userId));
    }

    @PutMapping("/{turfId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<TurfResponse> updateTurf(@PathVariable Long turfId,
                                                   @Valid @RequestBody TurfRequest request) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(turfService.updateTurf(turfId, request, userId));
    }

    @DeleteMapping("/{turfId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteTurf(@PathVariable Long turfId) {
        Long userId = getCurrentUserId();
        turfService.deleteTurf(turfId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<TurfResponse>> getOwnerTurfs() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(turfService.getOwnerTurfs(userId));
    }

    @GetMapping("/owner/{turfId}/bookings")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<BookingDto>> getTurfBookings(@PathVariable Long turfId) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(turfService.getTurfBookings(turfId, userId));
    }

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TurfResponse>> getAllTurfs() {
        return ResponseEntity.ok(turfService.getAllTurfs());
    }

    @PutMapping("/admin/{turfId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TurfResponse> updateTurfStatus(@PathVariable Long turfId,
                                                         @RequestParam TurfStatus status) {
        return ResponseEntity.ok(turfService.updateTurfStatus(turfId, status));
    }

    @DeleteMapping("/admin/{turfId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTurfAdmin(@PathVariable Long turfId) {
        turfService.deleteTurfAdmin(turfId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TurfResponse>> getApprovedTurfs() {
        return ResponseEntity.ok(turfService.getApprovedTurfs());
    }

    @GetMapping("/{turfId}")
    public ResponseEntity<TurfResponse> getTurf(@PathVariable Long turfId) {
        return ResponseEntity.ok(turfService.getTurf(turfId));
    }
    
}