package gr.georgestaav.subscriptionservice.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.georgestaav.subscriptionservice.service.SubscriptionService;
import gr.georgestaav.subscriptionservice.web.controller.SubscriptionController;
import gr.georgestaav.subscriptionservice.web.dto.SubscriptionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


    @WebMvcTest(SubscriptionController.class)
    public class SubscriptionControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private SubscriptionService subscriptionService;

        // CREATE
        @Test
        void givenValidMobile_whenCreateSubscription_thenReturn201() throws Exception {
            mockMvc.perform(post("/api/create")
                            .param("mobileNumber", "6912345678"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }

        @Test
        void givenInvalidMobile_whenCreateSubscription_thenReturn400() throws Exception {
            mockMvc.perform(post("/api/create")
                            .param("mobileNumber", "123"))
                    .andExpect(status().isBadRequest());
        }

        // FETCH
        @Test
        void givenValidMobile_whenGetSubscription_thenReturn200() throws Exception {
            SubscriptionDto subscriptionDto = new SubscriptionDto();
            subscriptionDto.setMobileNumber("6912345678");
            subscriptionDto.setActive(true);

            given(subscriptionService.getSubscription("6912345678"))
                    .willReturn(subscriptionDto);

            mockMvc.perform(get("/api/fetch")
                            .param("mobileNumber", "6912345678"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.mobileNumber").value("6912345678"))
                    .andExpect(jsonPath("$.active").value(true));
        }

        @Test
        void givenInvalidMobile_whenGetSubscription_thenReturn400() throws Exception {
            mockMvc.perform(get("/api/fetch")
                            .param("mobileNumber", "123"))
                    .andExpect(status().isBadRequest());
        }

        // RENEW
        @Test
        void givenValidMobile_whenRenewSubscription_thenReturn200() throws Exception {
            given(subscriptionService.renewSubscription("6912345678"))
                    .willReturn(true);

            mockMvc.perform(put("/api/update")
                            .param("mobileNumber", "6912345678"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        void givenRenewFails_whenRenewSubscription_thenReturn417() throws Exception {
            given(subscriptionService.renewSubscription("6912345678"))
                    .willReturn(false);

            mockMvc.perform(put("/api/update")
                            .param("mobileNumber", "6912345678"))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }

        @Test
        void givenInvalidMobile_whenRenewSubscription_thenReturn400() throws Exception {
            mockMvc.perform(put("/api/update")
                            .param("mobileNumber", "123"))
                    .andExpect(status().isBadRequest());
        }

        // DELETE
        @Test
        void givenValidMobile_whenDeleteSubscription_thenReturn200() throws Exception {
            given(subscriptionService.deleteSubscription("6912345678"))
                    .willReturn(true);

            mockMvc.perform(delete("/api/delete")
                            .param("mobileNumber", "6912345678"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        void givenDeleteFails_whenDeleteSubscription_thenReturn417() throws Exception {
            given(subscriptionService.deleteSubscription("6912345678"))
                    .willReturn(false);

            mockMvc.perform(delete("/api/delete")
                            .param("mobileNumber", "6912345678"))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }

        @Test
        void givenInvalidMobile_whenDeleteSubscription_thenReturn400() throws Exception {
            mockMvc.perform(delete("/api/delete")
                            .param("mobileNumber", "123"))
                    .andExpect(status().isBadRequest());
        }
    }

