package gr.georgestaav.membershipservice.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.georgestaav.membershipservice.service.MembershipService;
import gr.georgestaav.membershipservice.web.controller.MembershipController;
import gr.georgestaav.membershipservice.web.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MembershipController.class)
public class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MembershipService membershipService;

    private MemberDto memberDto;

    @BeforeEach
    void setUp() {
        memberDto = new MemberDto();
        memberDto.setName("George Stavos");
        memberDto.setEmail("george@test.com");
        memberDto.setMobileNumber("6912345678");
    }

    @Test
    void givenValidMemberDto_whenCreateMembership_thenReturn201() throws Exception {
        mockMvc.perform(post("/api/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("201"));
    }

    @Test
    void givenInvalidMobile_whenCreateMembership_thenReturn400() throws Exception {
        memberDto.setMobileNumber("123"); // invalid

        mockMvc.perform(post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidEmail_whenCreateMembership_thenReturn400() throws Exception {
        memberDto.setEmail("notanemail"); // invalid

        mockMvc.perform(post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidMobile_whenGetMembership_thenReturn200() throws Exception {
        given(membershipService.getMembership("6912345678"))
                .willReturn(memberDto);

        mockMvc.perform(get("/api/fetch")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobileNumber").value("6912345678"))
                .andExpect(jsonPath("$.name").value("George Stavos"));
    }

    @Test
    void givenInvalidMobile_whenGetMembership_thenReturn400() throws Exception {
        mockMvc.perform(get("/api/fetch")
                        .param("mobileNumber", "123"))
                .andExpect(status().isBadRequest());
    }

    // UPDATE
    @Test
    void givenValidRequest_whenUpdateMembership_thenReturn200() throws Exception {
        given(membershipService.updateMembership(any(MemberDto.class)))
                .willReturn(true);

        mockMvc.perform(put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    void givenUpdateFails_whenUpdateMembership_thenReturn417() throws Exception {
        given(membershipService.updateMembership(any(MemberDto.class)))
                .willReturn(false);

        mockMvc.perform(put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value("417"));
    }
}
