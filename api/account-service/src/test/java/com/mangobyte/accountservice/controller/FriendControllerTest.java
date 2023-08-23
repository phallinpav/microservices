package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.ErrorMessageUtils;
import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.dao.FriendRepository;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.service.FriendService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({FriendController.class})
public class FriendControllerTest extends BaseControllerTest {

    @MockBean
    private FriendService friendService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private FriendRepository friendRepository;

    private final MockHttpServletRequestBuilder GET_ALL_FRIENDS = get("/friends");
    private final MockHttpServletRequestBuilder ADD_FRIEND_TO_ADMIN = post("/friends/add/1");
    private final MockHttpServletRequestBuilder ADD_FRIEND_TO_USER = post("/friends/add/2");
    private final MockHttpServletRequestBuilder ADD_NO_EXISTING_USER = post("/friends/add/3");
    private final MockHttpServletRequestBuilder REQUEST_TO_USER = patch("/friends/request/2");
    private final MockHttpServletRequestBuilder REQUEST_TO_UNKNOWN_USER = patch("/friends/request/99");
    private final MockHttpServletRequestBuilder GET_REQUESTING_LIST = get("/friends/requests");

    @BeforeEach
    void setup() {
        provideServiceValue();
    }


    @Test
    void addFriend_noAuthHeader() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_USER)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addFriend_withAuthHeader_unknownBasic() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_USER.header(BASIC_HEADER, SampleTestData.UNKNOWN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addFriend_withAuthHeader_adminBasic() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_USER.header(BASIC_HEADER, SampleTestData.ADMIN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addFriend_withAuthHeader_userBasic() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_ADMIN.header(BASIC_HEADER, SampleTestData.USER_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownApi() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_USER.header(API_HEADER, SampleTestData.UNKNOWN_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_adminApi() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getAccounts_withAuthHeader_userApi() throws Exception {
        mockMvc.perform(ADD_FRIEND_TO_ADMIN.header(API_HEADER, SampleTestData.USER_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void add_noExisting_user() throws Exception {
        mockMvc.perform(ADD_NO_EXISTING_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void accept_friend_success() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "accepted");
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void deny_friend_success() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "denied");
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void removed_friend_success() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "removed");
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void get_requesting_list_success() throws Exception {
        mockMvc.perform(GET_REQUESTING_LIST.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void accept_friend_fail_account_not_exist() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "accepted");
        mockMvc.perform(REQUEST_TO_UNKNOWN_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deny_friend_fail_account_not_exist() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "denied");
        mockMvc.perform(REQUEST_TO_UNKNOWN_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void removed_friend_fail_account_not_exist() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "removed");
        mockMvc.perform(REQUEST_TO_UNKNOWN_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void check_case_ignore() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "accEpTed");
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void invalid_status_to_request() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "requesting");
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.REQUESTING_BAD_STATUS)));
    }

    @Test
    void request_on_incorrect_value() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", "tomorrow");
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.REQUESTING_BAD_STATUS)));
    }

    @Test
    void request_empty_body() throws Exception {
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.EMPTY_BODY)));
    }

    @Test
    void request_empty_object() throws Exception {
        JSONObject body = new JSONObject();
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isBadRequest());
    }


    @Test
    void request_null_value() throws Exception {
        JSONObject body = new JSONObject();
        body.put("status", null);
        mockMvc.perform(REQUEST_TO_USER.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.message", is(ErrorMessageUtils.REQUESTING_BAD_STATUS)));

    }

    @Test
    void get_allFriends_success() throws Exception {
        mockMvc.perform(GET_ALL_FRIENDS.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    private void provideServiceValue() {
        when(accountService.findById(SampleTestData.USER_ID)).thenReturn(Optional.ofNullable(SampleTestData.USER_ACCOUNT));
        when(accountService.findById(SampleTestData.ADMIN_ID)).thenReturn(Optional.ofNullable(SampleTestData.ADMIN_ACCOUNT));
    }
}
