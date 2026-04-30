package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.config.JwtAuthenticationFilter;
import com.internship.tool.config.JwtUtil;
import com.internship.tool.dto.KriRecordRequest;
import com.internship.tool.entity.KriRecord;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.service.KriRecordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = KriRecordController.class)
@Import(KriRecordControllerTest.PermitAllSecurityConfig.class)
class KriRecordControllerTest {

    /**
     * Replaces SecurityConfig for this test slice — permits all requests
     * and disables CSRF so the JWT filter is never invoked.
     */
    @Configuration
    static class PermitAllSecurityConfig {
        @Bean
        public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a.anyRequest().permitAll());
            return http.build();
        }
    }

    @Autowired MockMvc       mockMvc;
    @Autowired ObjectMapper  objectMapper;

    // Mocked to prevent JwtAuthenticationFilter from being wired into the security chain
    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean JwtUtil                 jwtUtil;
    @MockBean KriRecordService        kriRecordService;

    // ================================================================== //
    //  GET /api/kri/all → 200                                            //
    // ================================================================== //

    @Test
    @DisplayName("GET /api/kri/all: returns 200 with paginated records")
    void getAllRecords_returns200() throws Exception {
        KriRecord record = buildRecord(1L, "KRI Alpha");
        when(kriRecordService.getAllRecords(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(record)));

        mockMvc.perform(get("/api/kri/all")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("KRI Alpha"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ================================================================== //
    //  GET /api/kri/{id} → 200                                           //
    // ================================================================== //

    @Test
    @DisplayName("GET /api/kri/{id}: returns 200 with record body when found")
    void getRecordById_found_returns200() throws Exception {
        KriRecord record = buildRecord(5L, "KRI Beta");
        when(kriRecordService.getRecordById(5L)).thenReturn(record);

        mockMvc.perform(get("/api/kri/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("KRI Beta"));
    }

    // ================================================================== //
    //  GET /api/kri/{id} → 404 (GlobalExceptionHandler integration)      //
    // ================================================================== //

    @Test
    @DisplayName("GET /api/kri/{id}: 404 response contains correct error structure")
    void getRecordById_notFound_returns404WithErrorBody() throws Exception {
        when(kriRecordService.getRecordById(99L))
                .thenThrow(new ResourceNotFoundException("KRI record not found with id: 99"));

        mockMvc.perform(get("/api/kri/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("KRI record not found with id: 99"))
                .andExpect(jsonPath("$.path").value("/api/kri/99"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ================================================================== //
    //  POST /api/kri/create → 201                                        //
    // ================================================================== //

    @Test
    @DisplayName("POST /api/kri/create: returns 201 with saved record body")
    void createRecord_valid_returns201() throws Exception {
        KriRecordRequest request = new KriRecordRequest(
                "New KRI", "Description", "Finance", "active", 80.0, null);
        KriRecord saved = buildRecord(10L, "New KRI");
        when(kriRecordService.createRecord(any(KriRecord.class))).thenReturn(saved);

        mockMvc.perform(post("/api/kri/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("New KRI"));

        verify(kriRecordService, times(1)).createRecord(any(KriRecord.class));
    }

    // ================================================================== //
    //  POST /api/kri/create → 400 — blank title (GlobalExceptionHandler) //
    // ================================================================== //

    @Test
    @DisplayName("POST /api/kri/create: 400 with full error structure when title is blank")
    void createRecord_blankTitle_returns400WithDetails() throws Exception {
        KriRecordRequest request = new KriRecordRequest(
                "", "Description", "Finance", "active", null, null);

        mockMvc.perform(post("/api/kri/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details.title").exists())
                .andExpect(jsonPath("$.path").value("/api/kri/create"));

        // Service must never be called when bean validation rejects the request
        verifyNoInteractions(kriRecordService);
    }

    // ================================================================== //
    //  POST /api/kri/create → 400 — missing required field (title null)  //
    // ================================================================== //

    @Test
    @DisplayName("POST /api/kri/create: 400 with details map when title field is absent")
    void createRecord_missingTitle_returns400WithDetailsMap() throws Exception {
        // Send a body with no title field at all
        String bodyWithoutTitle = """
                {
                  "description": "Some desc",
                  "category": "Finance",
                  "status": "active"
                }
                """;

        mockMvc.perform(post("/api/kri/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWithoutTitle))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details.title").exists());

        verifyNoInteractions(kriRecordService);
    }

    // ------------------------------------------------------------------ //
    //  Helper                                                             //
    // ------------------------------------------------------------------ //

    private KriRecord buildRecord(Long id, String title) {
        return KriRecord.builder()
                .id(id)
                .title(title)
                .category("Finance")
                .status("active")
                .score(75.0)
                .build();
    }
}
