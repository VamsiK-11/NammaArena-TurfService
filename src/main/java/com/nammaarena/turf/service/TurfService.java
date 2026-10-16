package com.nammaarena.turf.service;

import com.nammaarena.turf.client.BookingServiceClient;
import com.nammaarena.turf.dto.*;
import com.nammaarena.turf.entity.Sport;
import com.nammaarena.turf.entity.Turf;
import com.nammaarena.turf.enums.TurfStatus;
import com.nammaarena.turf.repository.TurfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurfService {
    
    private final TurfRepository turfRepository;
    private final BookingServiceClient bookingServiceClient;

    @Transactional
    public TurfResponse createTurf(TurfRequest request, Long ownerId) {
        if (turfRepository.findByNameAndOwnerId(request.getName(), ownerId).isPresent()) {
            throw new RuntimeException("Turf with this name already exists for this owner");
        }

        Turf turf = new Turf();
        turf.setName(request.getName());
        turf.setLocation(request.getLocation());
        turf.setImageUrl(request.getImageUrl());
        turf.setStartTime(request.getStartTime());
        turf.setEndTime(request.getEndTime());
        turf.setOwnerId(ownerId);
        turf.setStatus(TurfStatus.PENDING);

        final Turf finalTurf = turf;
        List<Sport> sports = request.getSports().stream()
            .map(sportDto -> {
                Sport sport = new Sport();
                sport.setSportName(sportDto.getSportName());
                sport.setPrice(sportDto.getPrice());
                sport.setTurf(finalTurf);
                return sport;
            })
            .collect(Collectors.toList());

        turf.setSports(sports);
        turf = turfRepository.save(turf);
        
        return mapToResponse(turf);
    }

    @Transactional
    public TurfResponse updateTurf(Long turfId, TurfRequest request, Long ownerId) {
        Turf turf = turfRepository.findByIdAndOwnerId(turfId, ownerId)
            .orElseThrow(() -> new RuntimeException("Turf not found or not owned by user"));

        turf.setName(request.getName());
        turf.setLocation(request.getLocation());
        turf.setImageUrl(request.getImageUrl());
        turf.setStartTime(request.getStartTime());
        turf.setEndTime(request.getEndTime());

        turf.getSports().clear();
        final Turf finalTurf = turf;
        List<Sport> sports = request.getSports().stream()
            .map(sportDto -> {
                Sport sport = new Sport();
                sport.setSportName(sportDto.getSportName());
                sport.setPrice(sportDto.getPrice());
                sport.setTurf(finalTurf);
                return sport;
            })
            .collect(Collectors.toList());

        turf.setSports(sports);
        turf = turfRepository.save(turf);
        
        return mapToResponse(turf);
    }

    public void deleteTurf(Long turfId, Long ownerId) {
        Turf turf = turfRepository.findByIdAndOwnerId(turfId, ownerId)
            .orElseThrow(() -> new RuntimeException("Turf not found or not owned by user"));
        turfRepository.delete(turf);
    }

    public List<TurfResponse> getOwnerTurfs(Long ownerId) {
        return turfRepository.findByOwnerId(ownerId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public List<TurfResponse> getAllTurfs() {
        return turfRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public TurfResponse getTurf(Long turfId) {
        Turf turf = turfRepository.findById(turfId)
            .orElseThrow(() -> new RuntimeException("Turf not found"));
        return mapToResponse(turf);
    }

    public List<TurfResponse> getApprovedTurfs() {
        return turfRepository.findByStatus(TurfStatus.APPROVED)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public TurfResponse updateTurfStatus(Long turfId, TurfStatus status) {
        Turf turf = turfRepository.findById(turfId)
            .orElseThrow(() -> new RuntimeException("Turf not found"));
        turf.setStatus(status);
        turf = turfRepository.save(turf);
        return mapToResponse(turf);
    }

    public void deleteTurfAdmin(Long turfId) {
        Turf turf = turfRepository.findById(turfId)
            .orElseThrow(() -> new RuntimeException("Turf not found"));
        turfRepository.delete(turf);
    }

    public List<BookingDto> getTurfBookings(Long turfId, Long ownerId) {
        Turf turf = turfRepository.findByIdAndOwnerId(turfId, ownerId)
            .orElseThrow(() -> new RuntimeException("Turf not found or not owned by user"));
        
        try {
            return bookingServiceClient.getBookingsByTurfId(turfId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch bookings from booking service");
        }
    }

    private TurfResponse mapToResponse(Turf turf) {
        TurfResponse response = new TurfResponse();
        response.setId(turf.getId());
        response.setName(turf.getName());
        response.setLocation(turf.getLocation());
        response.setImageUrl(turf.getImageUrl());
        response.setStartTime(turf.getStartTime());
        response.setEndTime(turf.getEndTime());
        response.setAvailableTimings(turf.getStartTime() + " - " + turf.getEndTime());
        response.setOwnerId(turf.getOwnerId());
        response.setStatus(turf.getStatus());
        
        List<SportDto> sportDtos = turf.getSports().stream()
            .map(sport -> {
                SportDto dto = new SportDto();
                dto.setSportName(sport.getSportName());
                dto.setPrice(sport.getPrice());
                return dto;
            })
            .collect(Collectors.toList());
        
        response.setSports(sportDtos);
        return response;
    }
    
   
}