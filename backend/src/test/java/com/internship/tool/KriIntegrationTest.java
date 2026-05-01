package com.internship.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.KriFilterRequest;
import com.internship.tool.dto.KriRequest;
import com.internship.tool.repository.KriRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for KRI REST API.
 * Day 8 — Integration Testing & Containerization
 *
 * Uses an H2 in-memory database (test profile) so no real DB is needed.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)   // skip JWT filter in integration tests
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("KRI API Integration Tests")
class KriIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KriRepository kriRepository;

    private static Long createdKriId;

    // ── POST /api/v1/kri ──────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("POST /api/v1/kri — should create KRI and return 201")
    void createKri_Returns201() throws Exception {
        KriRequest req = KriRequest.builder()
                .name("Integration Test KRI")
                .description("Created during integration test")
                .status("ACTIVE")
                .score(50)
                .build();

        String response = mockMvc.perform(post("/api/v1/kri")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Integration Test KRI")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.score", is(50)))
                .andReturn().getResponse().getContentAsString();

        createdKriId = objectMapper.readTree(response).get("id").asLong();
    }

    // ── GET /api/v1/kri/{id} ──────────────────────────────────────────────────

    @Test
    @Order(2)
    @DisplayName("GET /api/v1/kri/{id} — should return the created KRI")
    void getById_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri/" + createdKriId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdKriId.intValue())))
                .andExpect(jsonPath("$.name", is("Integration Test KRI")));
    }

    // ── GET /api/v1/kri ───────────────────────────────────────────────────────

    @Test
    @Order(3)
    @DisplayName("GET /api/v1/kri — should return non-empty list")
    void getAll_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    // ── GET /api/v1/kri/search (Day 7 feature) ────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("GET /api/v1/kri/search — paginated search returns results")
    void search_Paginated_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri/search")
                        .param("status", "ACTIVE")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.totalElements", greaterThan(0)));
    }

    @Test
    @Order(5)
    @DisplayName("GET /api/v1/kri/search — name filter works")
    void search_ByName_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri/search")
                        .param("name", "Integration")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", containsString("Integration")));
    }

    @Test
    @Order(6)
    @DisplayName("GET /api/v1/kri/search — score range filter works")
    void search_ByScoreRange_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri/search")
                        .param("minScore", "40")
                        .param("maxScore", "60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())));
    }

    // ── GET /api/v1/kri/export/csv (Day 7 feature) ────────────────────────────

    @Test
    @Order(7)
    @DisplayName("GET /api/v1/kri/export/csv — should return CSV file")
    void exportCsv_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri/export/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("text/csv")))
                .andExpect(header().string("Content-Disposition", containsString("kri-export.csv")));
    }

    // ── PUT /api/v1/kri/{id} ──────────────────────────────────────────────────

    @Test
    @Order(8)
    @DisplayName("PUT /api/v1/kri/{id} — should update KRI successfully")
    void updateKri_Returns200() throws Exception {
        KriRequest updateReq = KriRequest.builder()
                .name("Updated Integration KRI")
                .description("Updated during integration test")
                .status("BREACH")
                .score(85)
                .build();

        mockMvc.perform(put("/api/v1/kri/" + createdKriId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Integration KRI")))
                .andExpect(jsonPath("$.status", is("BREACH")))
                .andExpect(jsonPath("$.score", is(85)));
    }

    // ── GET /api/v1/kri/at-risk ───────────────────────────────────────────────

    @Test
    @Order(9)
    @DisplayName("GET /api/v1/kri/at-risk — should include our updated BREACH KRI")
    void getAtRisk_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/kri/at-risk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    // ── DELETE /api/v1/kri/{id} ───────────────────────────────────────────────

    @Test
    @Order(10)
    @DisplayName("DELETE /api/v1/kri/{id} — should delete KRI and return 204")
    void deleteKri_Returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/kri/" + createdKriId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(11)
    @DisplayName("GET /api/v1/kri/{id} — should return 404 after deletion")
    void getById_AfterDelete_Returns404() throws Exception {
        mockMvc.perform(get("/api/v1/kri/" + createdKriId))
                .andExpect(status().isNotFound());
    }

    // ── POST validation ───────────────────────────────────────────────────────

    @Test
    @Order(12)
    @DisplayName("POST /api/v1/kri — invalid request should return 400")
    void createKri_InvalidRequest_Returns400() throws Exception {
        KriRequest invalid = KriRequest.builder()
                .name("")            // blank name → validation error
                .status("INVALID_STATUS")
                .score(200)          // exceeds max
                .build();

        mockMvc.perform(post("/api/v1/kri")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
