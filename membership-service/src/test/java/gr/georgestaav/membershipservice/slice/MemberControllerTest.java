package gr.georgestaav.membershipservice.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.georgestaav.membershipservice.service.MemberService;
import gr.georgestaav.membershipservice.web.controller.MemberController;
import gr.georgestaav.membershipservice.web.dto.MemberDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    private MemberDetailsDto memberDetailsDto;

    @BeforeEach
    void setUp() {
        memberDetailsDto = new MemberDetailsDto();
        memberDetailsDto.setName("George Stavros");
        memberDetailsDto.setEmail("george@test.com");
        memberDetailsDto.setMobileNumber("6912345678");
    }


    @Test
    void givenValidMobile_whenGetMemberDetails_thenReturn200() throws Exception {
        given(memberService.getMemberDetails("6912345678"))
                .willReturn(memberDetailsDto);

        mockMvc.perform(get("/api/fetchCustomerDetails")
                        .param("mobileNumber", "6912345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobileNumber").value("6912345678"))
                .andExpect(jsonPath("$.name").value("George Stavros"))
                .andExpect(jsonPath("$.email").value("george@test.com"));
    }

    @Test
    void givenInvalidMobile_whenGetMemberDetails_thenReturn400() throws Exception {
        mockMvc.perform(get("/api/fetchCustomerDetails")
                        .param("mobileNumber", "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenMissingMobile_whenGetMemberDetails_thenReturn400() throws Exception {
        mockMvc.perform(get("/api/fetchCustomerDetails"))
                .andExpect(status().isBadRequest());
    }

}
