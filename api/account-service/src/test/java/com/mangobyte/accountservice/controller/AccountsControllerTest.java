package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.SampleViewTestData;
import com.mangobyte.accountservice.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AccountsController.class})
public class AccountsControllerTest extends BaseControllerTest {

    @MockBean
    private AccountsService service;

    private final MockHttpServletRequestBuilder GET_SEARCH_ACCOUNTS = get("/accounts/search");

    @BeforeEach
    void setUp() {
        provideValue();
    }

    @Test
    void getAccounts_noAuthHeader() throws Exception {
        mockMvc.perform(GET_SEARCH_ACCOUNTS).andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownBasic() throws Exception {
        mockMvc.perform(GET_SEARCH_ACCOUNTS.header(BASIC_HEADER, SampleTestData.UNKNOWN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_adminBasic() throws Exception {
        mockMvc.perform(GET_SEARCH_ACCOUNTS.header(BASIC_HEADER, SampleTestData.ADMIN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_userBasic() throws Exception {
        mockMvc.perform(GET_SEARCH_ACCOUNTS.header(BASIC_HEADER, SampleTestData.USER_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_unknownApi() throws Exception {
        mockMvc.perform(GET_SEARCH_ACCOUNTS.header(API_HEADER, SampleTestData.UNKNOWN_TOKEN_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAccounts_withAuthHeader_userApi() throws Exception {
        mockMvc.perform(GET_SEARCH_ACCOUNTS.header(API_HEADER, SampleTestData.USER_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    private void provideValue() {
        when(service.search(SampleTestData.ADMIN_ACCOUNT, Optional.of(SampleTestData.USER_USERNAME)))
                .thenReturn(SampleViewTestData.ACCOUNT_FIELD_VIEW_LIST);
    }
}
