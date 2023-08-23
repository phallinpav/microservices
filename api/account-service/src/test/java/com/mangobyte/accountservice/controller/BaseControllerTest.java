package com.mangobyte.accountservice.controller;

import com.mangobyte.accountservice.SampleTestData;
import com.mangobyte.accountservice.auth.config.SecurityConfig;
import com.mangobyte.accountservice.auth.provider.ApiKeyAuthenticationProvider;
import com.mangobyte.accountservice.auth.provider.UsernamePasswordAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import({ SecurityConfig.class })
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Value("${apikey.header}")
    protected String API_HEADER;
    protected String BASIC_HEADER = "Authorization";

    @MockBean
    private ApiKeyAuthenticationProvider apiAuthProvider;

    @MockBean
    private UsernamePasswordAuthenticationProvider basicAuthProvider;

    @BeforeEach
    protected void initialSetup() {
        setProviderValue();
    }

    private void setProviderValue() {
        when(basicAuthProvider.supports(eq(UsernamePasswordAuthenticationToken.class))).thenReturn(true);
        when(apiAuthProvider.supports(any())).thenReturn(true);

        when(basicAuthProvider.authenticate(refEq(SampleTestData.ADMIN_BASIC_AUTH_TOKEN_RAW, "details")))
                .thenReturn(SampleTestData.ADMIN_BASIC_AUTH_TOKEN);
        when(apiAuthProvider.authenticate(refEq(SampleTestData.ADMIN_API_AUTH_TOKEN_RAW, "details")))
                .thenReturn(SampleTestData.ADMIN_API_AUTH_TOKEN);

        when(basicAuthProvider.authenticate(refEq(SampleTestData.USER_BASIC_AUTH_TOKEN_RAW, "details")))
                .thenReturn(SampleTestData.USER_BASIC_AUTH_TOKEN);
        when(apiAuthProvider.authenticate(refEq(SampleTestData.USER_API_AUTH_TOKEN_RAW, "details")))
                .thenReturn(SampleTestData.USER_API_AUTH_TOKEN);
    }

}
