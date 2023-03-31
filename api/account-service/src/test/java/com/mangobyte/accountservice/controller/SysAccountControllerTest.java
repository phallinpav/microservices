package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.ErrorMessageUtils;
import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SysAccountController.class)
public class SysAccountControllerTest extends BaseControllerTest {
    @MockBean
    private AccountService service;
    
    private final MockHttpServletRequestBuilder GET_ACCOUNTS = get("/sys/accounts");
    private final MockHttpServletRequestBuilder GET_ACCOUNT_ADMIN = get("/sys/accounts/" + SampleTestData.ADMIN_ID);
    private final MockHttpServletRequestBuilder GET_ACCOUNT_USER = get("/sys/accounts/" + SampleTestData.USER_ID);
    private final MockHttpServletRequestBuilder GET_ACCOUNT_UNKNOWN = get("/sys/accounts/100");
    private final MockHttpServletRequestBuilder POST_ACCOUNT = post("/sys/accounts/");

    @BeforeEach
    void setup() {
        provideServiceValue();
    }

    @Test
    void getAccounts_noAuthHeader() throws Exception {
        mockMvc.perform(GET_ACCOUNTS).andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownBasic() throws Exception {
        mockMvc.perform(GET_ACCOUNTS.header(BASIC_HEADER, SampleTestData.UNKNOWN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_adminBasic() throws Exception {
        mockMvc.perform(GET_ACCOUNTS.header(BASIC_HEADER, SampleTestData.ADMIN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_userBasic() throws Exception {
        mockMvc.perform(GET_ACCOUNTS.header(BASIC_HEADER, SampleTestData.USER_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownApi() throws Exception {
        mockMvc.perform(GET_ACCOUNTS.header(API_HEADER, SampleTestData.UNKNOWN_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_adminApi() throws Exception {
        mockMvc.perform(GET_ACCOUNTS.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.ACCOUNTS)));
    }

    @Test
    void getAccounts_withAuthHeader_userApi() throws Exception {
        mockMvc.perform(GET_ACCOUNTS.header(API_HEADER, SampleTestData.USER_TOKEN_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void findAccountAdmin() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(GET_ACCOUNT_ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.ADMIN_ACCOUNT)));
    }

    @Test
    void findAccountUser() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(GET_ACCOUNT_USER))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT)));
    }

    @Test
    void findAccountUnknown() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(GET_ACCOUNT_UNKNOWN)).andExpect(status().isNotFound());
    }

    @Test
    void createAccount_emptyBody() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(POST_ACCOUNT))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.EMPTY_BODY)));
    }

    @Test
    void createAccount_allFieldEmpty() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(POST_ACCOUNT).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages",
                        hasItems(ErrorMessageUtils.fieldNullError("username"),
                                ErrorMessageUtils.fieldNullError("email"),
                                ErrorMessageUtils.fieldNullError("password"))));
    }

    @Test
    void createAccount_allFieldEmptyString() throws Exception {
        JSONObject content = new JSONObject();
        content.put("username", "");
        content.put("email", "");
        content.put("password", "");
        mockMvc.perform(addAuthenticatedHeader(POST_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages",
                        hasItems(ErrorMessageUtils.fieldSizeError("username", 3, 30),
                                ErrorMessageUtils.fieldSizeError("email", 3, 125),
                                ErrorMessageUtils.fieldSizeError("password", 3, 30))));
    }

    @Test
    void createAccount_mixError() throws Exception {
        JSONObject content = new JSONObject();
        content.put("username", RandomStringUtils.random(31));
        content.put("email", RandomStringUtils.random(3));
        content.put("password", RandomStringUtils.random(30));
        mockMvc.perform(addAuthenticatedHeader(POST_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages",
                        hasItems(ErrorMessageUtils.fieldSizeError("username", 3, 30),
                                ErrorMessageUtils.fieldEmailError("email"))));
    }

    @Test
    void createAccount_allValidField() throws Exception {
        JSONObject content = new JSONObject();
        content.put("username", SampleTestData.USER_USERNAME);
        content.put("email", SampleTestData.USER_EMAIL);
        content.put("password", SampleTestData.USER_PASS);
        mockMvc.perform(addAuthenticatedHeader(POST_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT)));
    }

    private void provideServiceValue() {
        when(service.findAll()).thenReturn(SampleTestData.ACCOUNTS);
        when(service.findById(eq(SampleTestData.ADMIN_ID)))
                .thenReturn(Optional.of(SampleTestData.ADMIN_ACCOUNT));
        when(service.findById(eq(SampleTestData.USER_ID)))
                .thenReturn(Optional.of(SampleTestData.USER_ACCOUNT));
        when(service.save(eq(SampleTestData.USER_ACCOUNT_CREATE_FORM.toAccountUser())))
                .thenReturn(SampleTestData.USER_ACCOUNT);
    }

    private MockHttpServletRequestBuilder addAuthenticatedHeader(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE);
    }
}
