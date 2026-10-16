package com.nammaarena.turf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nammaarena.turf.dto.*;
import com.nammaarena.turf.enums.TurfStatus;
import com.nammaarena.turf.service.TurfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurfController.class)
class TurfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurfService turfService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "1", roles = "OWNER")
    void createTurf_Success() throws Exception {
        TurfRequest request = new TurfRequest();
        request.setName("Test Turf");

        TurfResponse response = new TurfResponse();
        response.setName("Test Turf");

        when(turfService.createTurf(any(TurfRequest.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/api/turfs")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Turf"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTurfs_Success() throws Exception {
        when(turfService.getAllTurfs()).thenReturn(Arrays.asList(new TurfResponse()));

        mockMvc.perform(get("/api/turfs/admin/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getApprovedTurfs_Success() throws Exception {
        when(turfService.getApprovedTurfs()).thenReturn(Arrays.asList(new TurfResponse()));

        mockMvc.perform(get("/api/turfs"))
                .andExpect(status().isOk());
    }

    @Test
    void getTurf_Success() throws Exception {
        TurfResponse response = new TurfResponse();
        response.setId(1L);
        when(turfService.getTurf(1L)).thenReturn(response);

        mockMvc.perform(get("/api/turfs/1"))
                .andExpect(status().isOk());
    }
}