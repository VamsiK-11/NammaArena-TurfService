package com.nammaarena.turf.service;

import com.nammaarena.turf.client.BookingServiceClient;
import com.nammaarena.turf.dto.*;
import com.nammaarena.turf.entity.Sport;
import com.nammaarena.turf.entity.Turf;
import com.nammaarena.turf.enums.TurfStatus;
import com.nammaarena.turf.repository.TurfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurfServiceTest {

    @Mock
    private TurfRepository turfRepository;

    @Mock
    private BookingServiceClient bookingServiceClient;

    @InjectMocks
    private TurfService turfService;

    private Turf testTurf;
    private TurfRequest turfRequest;
    private Sport testSport;

    @BeforeEach
    void setUp() {
        testSport = new Sport();
        testSport.setSportName("Football");
        testSport.setPrice(new BigDecimal("100.0"));

        testTurf = new Turf();
        testTurf.setId(1L);
        testTurf.setName("Test Turf");
        testTurf.setLocation("Test Location");
        testTurf.setOwnerId(1L);
        testTurf.setStatus(TurfStatus.PENDING);
        testTurf.setStartTime("09:00");
        testTurf.setEndTime("22:00");
        testTurf.setSports(Arrays.asList(testSport));

        SportDto sportDto = new SportDto();
        sportDto.setSportName("Football");
        sportDto.setPrice(new BigDecimal("100.0"));

        turfRequest = new TurfRequest();
        turfRequest.setName("Test Turf");
        turfRequest.setLocation("Test Location");
        turfRequest.setStartTime("09:00");
        turfRequest.setEndTime("22:00");
        turfRequest.setSports(Arrays.asList(sportDto));
    }

    @Test
    void createTurf_Success() {
        when(turfRepository.findByNameAndOwnerId("Test Turf", 1L)).thenReturn(Optional.empty());
        when(turfRepository.save(any(Turf.class))).thenReturn(testTurf);

        var response = turfService.createTurf(turfRequest, 1L);

        assertNotNull(response);
        assertEquals("Test Turf", response.getName());
        verify(turfRepository).save(any(Turf.class));
    }

    @Test
    void createTurf_DuplicateName_ThrowsException() {
        when(turfRepository.findByNameAndOwnerId("Test Turf", 1L)).thenReturn(Optional.of(testTurf));

        assertThrows(RuntimeException.class, () -> turfService.createTurf(turfRequest, 1L));
    }

    @Test
    void updateTurf_Success() {
        when(turfRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(testTurf));
        when(turfRepository.save(any(Turf.class))).thenReturn(testTurf);

        var response = turfService.updateTurf(1L, turfRequest, 1L);

        assertNotNull(response);
        assertEquals("Test Turf", response.getName());
    }

    @Test
    void deleteTurf_Success() {
        when(turfRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(testTurf));

        turfService.deleteTurf(1L, 1L);

        verify(turfRepository).delete(testTurf);
    }

    @Test
    void getApprovedTurfs_Success() {
        when(turfRepository.findByStatus(TurfStatus.APPROVED)).thenReturn(Arrays.asList(testTurf));

        var response = turfService.getApprovedTurfs();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void updateTurfStatus_Success() {
        when(turfRepository.findById(1L)).thenReturn(Optional.of(testTurf));
        when(turfRepository.save(any(Turf.class))).thenReturn(testTurf);

        var response = turfService.updateTurfStatus(1L, TurfStatus.APPROVED);

        assertNotNull(response);
        assertEquals(TurfStatus.APPROVED, testTurf.getStatus());
    }

    @Test
    void getTurf_NotFound_ThrowsException() {
        when(turfRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> turfService.getTurf(1L));
    }

    @Test
    void getTurf_Success() {
        when(turfRepository.findById(1L)).thenReturn(Optional.of(testTurf));

        var response = turfService.getTurf(1L);

        assertNotNull(response);
        assertEquals("Test Turf", response.getName());
    }

    @Test
    void getOwnerTurfs_Success() {
        when(turfRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(testTurf));

        var response = turfService.getOwnerTurfs(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void getAllTurfs_Success() {
        when(turfRepository.findAll()).thenReturn(Arrays.asList(testTurf));

        var response = turfService.getAllTurfs();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void updateTurf_NotFound_ThrowsException() {
        when(turfRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> turfService.updateTurf(1L, turfRequest, 1L));
    }

    @Test
    void deleteTurf_NotFound_ThrowsException() {
        when(turfRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> turfService.deleteTurf(1L, 1L));
    }

    @Test
    void deleteTurfAdmin_Success() {
        when(turfRepository.findById(1L)).thenReturn(Optional.of(testTurf));

        turfService.deleteTurfAdmin(1L);

        verify(turfRepository).delete(testTurf);
    }

    @Test
    void deleteTurfAdmin_NotFound_ThrowsException() {
        when(turfRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> turfService.deleteTurfAdmin(1L));
    }

    @Test
    void updateTurfStatus_NotFound_ThrowsException() {
        when(turfRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> turfService.updateTurfStatus(1L, TurfStatus.APPROVED));
    }
}