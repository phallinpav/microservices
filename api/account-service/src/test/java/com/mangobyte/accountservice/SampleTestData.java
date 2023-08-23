package com.mangobyte.accountservice;

import com.mangobyte.accountservice.auth.model.ApiKeyAuthenticationToken;
import com.mangobyte.accountservice.form.AccountCreateForm;
import com.mangobyte.accountservice.form.AccountUpdateForm;
import com.mangobyte.accountservice.model.Role;
import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Token;
import com.mangobyte.accountservice.utils.CommonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SampleTestData {
    public final static long ADMIN_ID = 1;
    public final static String ADMIN_USERNAME = "admin";
    public final static String ADMIN_PASS = "admin";
    public final static Account ADMIN_ACCOUNT = getAdminAccount();
    public final static String ADMIN_TOKEN_VALUE = UUID.randomUUID().toString();
    public final static Token ADMIN_TOKEN = getAdminToken();
    public final static String ADMIN_BASIC_AUTH_HEADER = getAdminBasicAuth();
    public final static UsernamePasswordAuthenticationToken ADMIN_BASIC_AUTH_TOKEN_RAW = getAdminUserPassAuthTokenRaw();
    public final static UsernamePasswordAuthenticationToken ADMIN_BASIC_AUTH_TOKEN = getAdminUserPassAuthToken();
    public final static PreAuthenticatedAuthenticationToken ADMIN_API_AUTH_TOKEN_RAW = getAdminApiAuthTokenRaw();
    public final static ApiKeyAuthenticationToken ADMIN_API_AUTH_TOKEN = getAdminApiAuthToken();

    public final static long USER_ID = 2;
    public final static String USER_USERNAME = "Tester1";
    public final static String USER_USERNAME_UPDATED = "Tester1-Updated";
    public final static String USER_EMAIL = "test@email.com";
    public final static String USER_EMAIL_UPDATED = "test-updated@email.com";
    public final static String USER_PASS = "P-Tester!1";
    public final static AccountCreateForm USER_ACCOUNT_CREATE_FORM = getUserAccountCreateForm();
    public final static AccountUpdateForm USER_ACCOUNT_UPDATE_FORM_1 = getUserAccountUpdateForm(USER_USERNAME_UPDATED, USER_EMAIL_UPDATED);
    public final static AccountUpdateForm USER_ACCOUNT_UPDATE_FORM_2 = getUserAccountUpdateForm(USER_USERNAME_UPDATED, null);
    public final static AccountUpdateForm USER_ACCOUNT_UPDATE_FORM_3 = getUserAccountUpdateForm(null, USER_EMAIL_UPDATED);
    public final static Account USER_ACCOUNT = getUserAccount();
    public final static Account USER_ACCOUNT_UPDATE_1 = getUserAccountUpdate(USER_USERNAME_UPDATED, USER_EMAIL_UPDATED);
    public final static Account USER_ACCOUNT_UPDATE_2 = getUserAccountUpdate(USER_USERNAME_UPDATED, null);
    public final static Account USER_ACCOUNT_UPDATE_3 = getUserAccountUpdate(null, USER_EMAIL_UPDATED);
    public final static String USER_TOKEN_VALUE = UUID.randomUUID().toString();
    public final static Token USER_TOKEN = getUserToken();
    public final static String USER_BASIC_AUTH_HEADER = getUserBasicAuth();
    public final static UsernamePasswordAuthenticationToken USER_BASIC_AUTH_TOKEN = getUserUserPassAuthToken();
    public final static UsernamePasswordAuthenticationToken USER_BASIC_AUTH_TOKEN_RAW = getUserUserPassAuthTokenRaw();
    public final static PreAuthenticatedAuthenticationToken USER_API_AUTH_TOKEN_RAW = getUserApiAuthTokenRaw();
    public final static ApiKeyAuthenticationToken USER_API_AUTH_TOKEN = getUserApiAuthToken();
    public final static List<Account> ACCOUNTS = Arrays.asList(ADMIN_ACCOUNT, USER_ACCOUNT);
    public final static String UNKNOWN_BASIC_AUTH_HEADER = basicAuth("unknown", "u-password");
    public final static String UNKNOWN_TOKEN_VALUE = UUID.randomUUID().toString();
    public final static Account UNKNOWN_ACCOUNT = getUnknownAccount();
    public final static long UNKNOWN_ID = 99;

    private static Account getAdminAccount() {
        return Account.builder()
                .id(ADMIN_ID)
                .username(ADMIN_USERNAME)
                .email("admin@email.com")
                .password("$2a$10$QmMzLLAe8xAr0HHZ3opNx.pGnwtygTyvvw7V93BuXstefi0bI1qMC")
                .role(Role.ADMIN)
                .active(true) // FIXME: THIS IS CLEARLY NOT FULLY CONSIDER, JUST QUICK FIX
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    private static AccountCreateForm getUserAccountCreateForm() {
        return new AccountCreateForm(USER_USERNAME, USER_EMAIL, USER_PASS);
    }

    private static AccountUpdateForm getUserAccountUpdateForm(String username, String email) {
        return new AccountUpdateForm(Optional.ofNullable(username), Optional.ofNullable(email));
    }

    private static Account getUserAccountUpdate(String username, String email) {
        Account updatedAccount = new AccountUpdateForm(Optional.ofNullable(username), Optional.ofNullable(email)).getUpdatedAccount(USER_ACCOUNT);
        updatedAccount.setUpdatedAt(new Date());
        return updatedAccount;
    }

    private static Account getUserAccount() {
        return Account.builder()
                .id(USER_ID)
                .username(USER_USERNAME)
                .email(USER_EMAIL)
                .password(CommonUtils.encodedPassword(USER_PASS))
                .role(Role.USER)
                .createdAt(new Date())
                .updatedAt(new Date())
                .active(true) // FIXME: THIS IS CLEARLY NOT FULLY CONSIDER, JUST QUICK FIX
                .build();
    }

    private static Account getUnknownAccount() {
        final Date now = CommonUtils.getDate();
        Account unknownAccount = randomUserAccountRaw();
        unknownAccount.setId(UNKNOWN_ID);
        unknownAccount.setCreatedAt(now);
        unknownAccount.setUpdatedAt(now);
        return unknownAccount;
    }

    private static Token getAdminToken() {
        Token token = new Token();
        token.setId(1L);
        token.setToken(ADMIN_TOKEN_VALUE);
        token.setRefreshToken(UUID.randomUUID().toString());
        token.setAccount(ADMIN_ACCOUNT);
        token.setCreatedAt(new Date());
        token.setExpiredAt(Date.from(new Date().toInstant().plus(Duration.ofHours(1))));
        return token;
    }

    private static Token getUserToken() {
        Token token = new Token();
        token.setId(2L);
        token.setToken(USER_TOKEN_VALUE);
        token.setRefreshToken(UUID.randomUUID().toString());
        token.setAccount(USER_ACCOUNT);
        token.setCreatedAt(new Date());
        token.setExpiredAt(Date.from(new Date().toInstant().plus(Duration.ofHours(1))));
        return token;
    }

    private static UsernamePasswordAuthenticationToken getAdminUserPassAuthTokenRaw() {
        return new UsernamePasswordAuthenticationToken(ADMIN_USERNAME, ADMIN_PASS);
    }

    private static UsernamePasswordAuthenticationToken getUserUserPassAuthTokenRaw() {
        return new UsernamePasswordAuthenticationToken(USER_USERNAME, USER_PASS);
    }

    private static PreAuthenticatedAuthenticationToken getAdminApiAuthTokenRaw() {
        return new PreAuthenticatedAuthenticationToken(ADMIN_TOKEN_VALUE, "N/A");
    }

    private static ApiKeyAuthenticationToken getAdminApiAuthToken() {
        return new ApiKeyAuthenticationToken(ADMIN_ACCOUNT, ADMIN_TOKEN_VALUE);
    }

    private static PreAuthenticatedAuthenticationToken getUserApiAuthTokenRaw() {
        return new PreAuthenticatedAuthenticationToken(USER_TOKEN_VALUE, "N/A");
    }

    private static ApiKeyAuthenticationToken getUserApiAuthToken() {
        return new ApiKeyAuthenticationToken(USER_ACCOUNT, USER_TOKEN_VALUE);
    }

    private static UsernamePasswordAuthenticationToken getAdminUserPassAuthToken() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ADMIN.toString()));
        return new UsernamePasswordAuthenticationToken(ADMIN_ACCOUNT, "N/A", authorities);
    }

    private static UsernamePasswordAuthenticationToken getUserUserPassAuthToken() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        return new UsernamePasswordAuthenticationToken(USER_ACCOUNT, "N/A", authorities);
    }

    private static String getUserBasicAuth() {
        return basicAuth(USER_USERNAME, USER_PASS);
    }

    private static String getAdminBasicAuth() {
        return basicAuth(ADMIN_USERNAME, ADMIN_PASS);
    }

    private static String basicAuth(String username, String password) {
        String auth = Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes());
        return String.format("Basic %s", auth);
    }

    public static Token randomTokenRaw(Account account) {
        return Token.builder()
                .account(account)
                .token(UUID.randomUUID().toString())
                .refreshToken(UUID.randomUUID().toString())
                .build();
    }

    public static Account randomUserAccountRaw() {
        return Account.builder()
                .username(RandomStringUtils.randomAlphanumeric(3, 30))
                .email(RandomStringUtils.randomAlphanumeric(3, 115) + "@email.com")
                .password(CommonUtils.encodedPassword(RandomStringUtils.randomAlphanumeric(3, 30)))
                .role(Role.USER)
                .active(true) // FIXME: THIS IS CLEARLY NOT FULLY CONSIDER, JUST QUICK FIX
                .build();
    }
}
