package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.service.AccountService;
import com.mangobyte.accountservice.service.BlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BlockController.class})
public class BlockControllerTest extends BaseControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private BlockService blockService;

    private final MockHttpServletRequestBuilder BLOCK_ADMIN_ACCOUNT = post("/block/1");
    private final MockHttpServletRequestBuilder BLOCK_USER_ACCOUNT = post("/block/2");
    private final MockHttpServletRequestBuilder BLOCK_USER_UNKNOWN_ACCOUNT = post("/block/99");

    @BeforeEach
    void setup() {
        provideServiceValue();
    }

    @Test
    void block_account_noAuthHeader() throws Exception {
        mockMvc.perform(BLOCK_USER_ACCOUNT)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void block_account_withAuthHeader_unknownBasic() throws Exception {
        mockMvc.perform(BLOCK_USER_ACCOUNT.header(BASIC_HEADER, SampleTestData.UNKNOWN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void block_account_withAuthHeader_adminBasic() throws Exception {
        mockMvc.perform(BLOCK_USER_ACCOUNT.header(BASIC_HEADER, SampleTestData.ADMIN_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void block_account_withAuthHeader_userBasic() throws Exception {
        mockMvc.perform(BLOCK_USER_ACCOUNT.header(BASIC_HEADER, SampleTestData.USER_BASIC_AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void block_noExisting_user() throws Exception {
        mockMvc.perform(BLOCK_USER_UNKNOWN_ACCOUNT.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void block_user_success() throws Exception {
        mockMvc.perform(BLOCK_USER_ACCOUNT.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void block_admin_success() throws Exception {
        mockMvc.perform(BLOCK_ADMIN_ACCOUNT.header(API_HEADER, SampleTestData.ADMIN_TOKEN_VALUE))
                .andExpect(status().isOk());
    }

    private void provideServiceValue() {
        when(accountService.findById(SampleTestData.USER_ID)).thenReturn(Optional.ofNullable(SampleTestData.USER_ACCOUNT));
        when(accountService.findById(SampleTestData.ADMIN_ID)).thenReturn(Optional.ofNullable(SampleTestData.ADMIN_ACCOUNT));
    }

}
