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

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest extends BaseControllerTest {
    @MockBean
    private AccountService service;
    
    private final MockHttpServletRequestBuilder GET_ACCOUNT = get("/account");
    private final MockHttpServletRequestBuilder PATCH_ACCOUNT = patch("/account");

    @BeforeEach
    void setup() {
        provideServiceValue();
    }

    @Test
    void getAccounts_noAuthHeader() throws Exception {
        mockMvc.perform(GET_ACCOUNT).andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownBasic() throws Exception {
        mockMvc.perform(GET_ACCOUNT.header(BASIC_HEADER, SampleTestData.UNKNOWN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_adminBasic() throws Exception {
        mockMvc.perform(GET_ACCOUNT.header(BASIC_HEADER, SampleTestData.ADMIN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_userBasic() throws Exception {
        mockMvc.perform(GET_ACCOUNT.header(BASIC_HEADER, SampleTestData.USER_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownApi() throws Exception {
        mockMvc.perform(GET_ACCOUNT.header(API_HEADER, SampleTestData.UNKNOWN_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_adminApi() throws Exception {
        mockMvc.perform(GET_ACCOUNT.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.ADMIN_ACCOUNT)));
    }

    @Test
    void getAccounts_withAuthHeader_userApi() throws Exception {
        mockMvc.perform(GET_ACCOUNT.header(API_HEADER, SampleTestData.USER_TOKEN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT)));
    }

    @Test
    void updateAccount_emptyBody() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.EMPTY_BODY)));
    }

    @Test
    void updateAccount_allFieldEmpty() throws Exception {
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.REQUIRE_USER_OR_EMAIL)));
    }

    @Test
    void updateAccount_allFieldEmptyString() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("username", "");
        content.put("email", "");
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages",
                        hasItems(ErrorMessageUtils.fieldSizeError("username", 3, 30),
                                ErrorMessageUtils.fieldSizeError("email", 3, 125))));
    }

    @Test
    void updateAccount_mixError() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("username", RandomStringUtils.randomAlphanumeric(3,30));
        content.put("email", RandomStringUtils.random(126));
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages", hasItems(ErrorMessageUtils.fieldSizeError("email", 3, 125))));
    }

    @Test
    void updateAccount_usernameError() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("username", RandomStringUtils.random(2));
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages",
                        hasItems(ErrorMessageUtils.fieldSizeError("username", 3, 30))));
    }

    @Test
    void updateAccount_emailError() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("email", RandomStringUtils.random(100));
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.messages",
                        hasItems(ErrorMessageUtils.fieldEmailError("email"))));
    }

    @Test
    void updateAccount_success_1() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("username", SampleTestData.USER_USERNAME_UPDATED);
        content.put("email", SampleTestData.USER_EMAIL_UPDATED);
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT_UPDATE_1)));
    }

    @Test
    void updateAccount_success_2() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("username", SampleTestData.USER_USERNAME_UPDATED);
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT_UPDATE_2)));
    }

    @Test
    void updateAccount_success_3() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("email", SampleTestData.USER_EMAIL_UPDATED);
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT_UPDATE_3)));
    }

    @Test
    void updateAccount_form3_result2() throws Exception {
        JSONObject content =  new JSONObject();
        content.put("email", SampleTestData.USER_EMAIL_UPDATED);
        mockMvc.perform(addAuthenticatedHeader(PATCH_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(not(CommonUtils.toJsonString(SampleTestData.USER_ACCOUNT_UPDATE_2))));
    }

    private void provideServiceValue() {
        when(service.save(eq(SampleTestData.USER_ACCOUNT_UPDATE_FORM_1.getUpdatedAccount(SampleTestData.USER_ACCOUNT))))
                .thenReturn(SampleTestData.USER_ACCOUNT_UPDATE_1);
        when(service.save(eq(SampleTestData.USER_ACCOUNT_UPDATE_FORM_2.getUpdatedAccount(SampleTestData.USER_ACCOUNT))))
                .thenReturn(SampleTestData.USER_ACCOUNT_UPDATE_2);
        when(service.save(eq(SampleTestData.USER_ACCOUNT_UPDATE_FORM_3.getUpdatedAccount(SampleTestData.USER_ACCOUNT))))
                .thenReturn(SampleTestData.USER_ACCOUNT_UPDATE_3);
    }

    private MockHttpServletRequestBuilder addAuthenticatedHeader(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header(API_HEADER, SampleTestData.USER_TOKEN_VALUE);
    }
}
