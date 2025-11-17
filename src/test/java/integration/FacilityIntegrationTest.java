package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.facility_rental.FacilityRentalApplication;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.FacilityMapper;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FacilityRentalApplication.class)
public class FacilityIntegrationTest {

    @LocalServerPort
    int port;



    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "http://localhost";
    }


    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    private CreateFacilityDto sampleInput() {
        return new CreateFacilityDto(
                "Boisko",
                "12A",
                "Długa",
                "Warszawa",
                "00-001",
                new BigDecimal("100")
        );
    }

    private ReturnedFacilityDto sampleReturned() {
        return new ReturnedFacilityDto(
                "1",
                "Boisko",
                "12A",
                "Długa",
                "Warszawa",
                "00-001",
                new BigDecimal("100")
        );
    }

    // -----------------------------------------------------
    // GET /facilities
    // -----------------------------------------------------
    @Test
    void shouldReturnAllFacilities() throws Exception {
        ReturnedFacilityDto dto = sampleReturned();

        Mockito.when(facilityService.findAll()).thenReturn(List.of(Mockito.mock(Object.class)));
        Mockito.when(facilityMapper.getFacilityDetails(any())).thenReturn(dto);

        mockMvc.perform(get("/facilities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    // -----------------------------------------------------
    // GET /facilities/{id}
    // -----------------------------------------------------
    @Test
    void shouldReturnFacilityById() throws Exception {
        ReturnedFacilityDto dto = sampleReturned();

        Mockito.when(facilityService.findById("1")).thenReturn(Optional.of(Mockito.mock(Object.class)));
        Mockito.when(facilityMapper.getFacilityDetails(any())).thenReturn(dto);

        mockMvc.perform(get("/facilities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void shouldReturn404WhenFacilityNotFound() throws Exception {
        Mockito.when(facilityService.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/facilities/999"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------------------------------
    // POST /facilities
    // -----------------------------------------------------
    @Test
    void shouldCreateFacility() throws Exception {
        CreateFacilityDto input = sampleInput();
        ReturnedFacilityDto output = sampleReturned();

        Mockito.when(facilityMapper.CreateFacilityRequest(any())).thenReturn(Mockito.mock(Object.class));
        Mockito.when(facilityService.save(any())).thenReturn(Mockito.mock(Object.class));
        Mockito.when(facilityMapper.getFacilityDetails(any())).thenReturn(output);

        mockMvc.perform(post("/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }

    // -----------------------------------------------------
    // DELETE /facilities/{id}
    // -----------------------------------------------------
    @Test
    void shouldDeleteFacility() throws Exception {
        ReturnedFacilityDto output = sampleReturned();

        Mockito.when(facilityService.deleteById("1")).thenReturn(Mockito.mock(Object.class));
        Mockito.when(facilityMapper.getFacilityDetails(any())).thenReturn(output);

        mockMvc.perform(delete("/facilities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    // -----------------------------------------------------
    // PUT /facilities/{id}
    // -----------------------------------------------------
    @Test
    void shouldUpdateFacility() throws Exception {
        CreateFacilityDto input = sampleInput();
        ReturnedFacilityDto output = sampleReturned();

        Mockito.when(facilityMapper.CreateFacilityRequest(any())).thenReturn(Mockito.mock(Object.class));
        Mockito.when(facilityService.update(any(), any())).thenReturn(Mockito.mock(Object.class));
        Mockito.when(facilityMapper.getFacilityDetails(any())).thenReturn(output);

        mockMvc.perform(put("/facilities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }
}

