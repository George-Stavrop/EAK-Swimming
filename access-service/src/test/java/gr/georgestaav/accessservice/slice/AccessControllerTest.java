package gr.georgestaav.accessservice.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.georgestaav.accessservice.constants.AccessConstants;
import gr.georgestaav.accessservice.service.AccessCardService;
import gr.georgestaav.accessservice.web.controller.AccessCardController;
import gr.georgestaav.accessservice.web.dto.AccessCardDto;
import gr.georgestaav.accessservice.web.dto.AccessCheckDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AccessCardController.class)
public class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccessCardService accessCardService;

    // CREATE
    @Test
    void givenValidMobile_whenCreateAccessCard_thenReturn201() throws Exception {
        mockMvc.perform(post("/api/create")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("201"));
    }

    @Test
    void givenInvalidMobile_whenCreateAccessCard_thenReturn400() throws Exception {
        mockMvc.perform(post("/api/create")
                        .param("mobileNumber", "123"))
                .andExpect(status().isBadRequest());
    }

    // FETCH
    @Test
    void givenValidMobile_whenGetAccessCard_thenReturn200() throws Exception {
        AccessCardDto accessCardDto = new AccessCardDto();
        accessCardDto.setMobileNumber("6912345678");
        accessCardDto.setAccessCardNumber("100000000001");
        accessCardDto.setAccessCardType("RFID_CARD");

        given(accessCardService.getAccessCard("6912345678"))
                .willReturn(accessCardDto);

        mockMvc.perform(get("/api/fetch")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobileNumber").value("6912345678"))
                .andExpect(jsonPath("$.accessCardNumber").value("100000000001"));
    }

    @Test
    void givenInvalidMobile_whenGetAccessCard_thenReturn400() throws Exception {
        mockMvc.perform(get("/api/fetch")
                        .param("mobileNumber", "123"))
                .andExpect(status().isBadRequest());
    }

    // TOGGLE
    @Test
    void givenValidMobile_whenToggleAccessCard_thenReturn200() throws Exception {
        given(accessCardService.toggleAccessCard("6912345678"))
                .willReturn(true);

        mockMvc.perform(put("/api/toggle")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    void givenToggleFails_whenToggleAccessCard_thenReturn417() throws Exception {
        given(accessCardService.toggleAccessCard("6912345678"))
                .willReturn(false);

        mockMvc.perform(put("/api/toggle")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value("417"));
    }

    @Test
    void givenInvalidMobile_whenToggleAccessCard_thenReturn400() throws Exception {
        mockMvc.perform(put("/api/toggle")
                        .param("mobileNumber", "123"))
                .andExpect(status().isBadRequest());
    }

    // CAN ENTER
    @Test
    void givenValidCardNumber_whenCanEnter_thenReturnAccessGranted() throws Exception {
        given(accessCardService.canEnter("100000000001"))
                .willReturn(new AccessCheckDto(true, AccessConstants.ACCESS_GRANTED));

        mockMvc.perform(get("/api/can-enter/100000000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value(true))
                .andExpect(jsonPath("$.reason").value(AccessConstants.ACCESS_GRANTED));
    }

    @Test
    void givenValidCardNumber_whenCanEnter_thenReturnSubscriptionExpired() throws Exception {
        given(accessCardService.canEnter("100000000001"))
                .willReturn(new AccessCheckDto(false, AccessConstants.SUBSCRIPTION_EXPIRED));

        mockMvc.perform(get("/api/can-enter/100000000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value(false))
                .andExpect(jsonPath("$.reason").value(AccessConstants.SUBSCRIPTION_EXPIRED));
    }

    @Test
    void givenInvalidCardNumber_whenCanEnter_thenReturn400() throws Exception {
        mockMvc.perform(get("/api/can-enter/123"))
                .andExpect(status().isBadRequest());
    }

    // DELETE
    @Test
    void givenValidMobile_whenDeleteAccessCard_thenReturn200() throws Exception {
        given(accessCardService.deleteAccessCard("6912345678"))
                .willReturn(true);

        mockMvc.perform(delete("/api/delete")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    void givenDeleteFails_whenDeleteAccessCard_thenReturn417() throws Exception {
        given(accessCardService.deleteAccessCard("6912345678"))
                .willReturn(false);

        mockMvc.perform(delete("/api/delete")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value("417"));
    }

    @Test
    void givenInvalidMobile_whenDeleteAccessCard_thenReturn400() throws Exception {
        mockMvc.perform(delete("/api/delete")
                        .param("mobileNumber", "123"))
                .andExpect(status().isBadRequest());
    }
}
