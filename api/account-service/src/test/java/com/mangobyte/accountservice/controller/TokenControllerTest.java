package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.service.TokenService;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
public class TokenControllerTest extends BaseControllerTest {
    @MockBean
    private TokenService service;

    private final MockHttpServletRequestBuilder GENERATE_TOKEN = get("/token/generate");

    protected final String[] EXCLUDED_TOKEN_FIELD = {"id", "token", "refreshToken", "createdAt", "expiredAt"};

    @BeforeEach
    void setup() {
        provideServiceValue();
    }

    @Test
    void generateToken_noAuthHeader() throws Exception {
        mockMvc.perform(GENERATE_TOKEN)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void generateToken_withAuthHeader_unknownBasic() throws Exception {
        mockMvc.perform(GENERATE_TOKEN.header(BASIC_HEADER, SampleTestData.UNKNOWN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void generateToken_withAuthHeader_adminBasic() throws Exception {
        mockMvc.perform(GENERATE_TOKEN.header(BASIC_HEADER, SampleTestData.ADMIN_BASIC_AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.ADMIN_TOKEN)));
    }

    @Test
    void generateToken_withAuthHeader_userBasic() throws Exception {
        mockMvc.perform(GENERATE_TOKEN.header(BASIC_HEADER, SampleTestData.USER_BASIC_AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_TOKEN)));
    }

    @Test
    void generateToken_withAuthHeader_unknownApi() throws Exception {
        mockMvc.perform(GENERATE_TOKEN.header(API_HEADER, SampleTestData.UNKNOWN_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void generateToken_withAuthHeader_adminApi() throws Exception {
        mockMvc.perform(GENERATE_TOKEN.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void generateToken_withAuthHeader_userApi() throws Exception {
        mockMvc.perform(GENERATE_TOKEN.header(API_HEADER, SampleTestData.USER_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    private void provideServiceValue() {
        when(service.generate(refEq(SampleTestData.ADMIN_TOKEN, EXCLUDED_TOKEN_FIELD)))
                .thenReturn(SampleTestData.ADMIN_TOKEN);
        when(service.generate(refEq(SampleTestData.USER_TOKEN, EXCLUDED_TOKEN_FIELD)))
                .thenReturn(SampleTestData.USER_TOKEN);
    }
}
