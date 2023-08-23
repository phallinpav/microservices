package com.mangobyte.accountservice.auth.filter;

import com.mangobyte.accountservice.auth.provider.ApiKeyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String apiHeader;

    public ApiKeyAuthFilter(@Value("${apikey.header}") String apiHeader,
                            @Autowired ApiKeyAuthenticationProvider provider) {
        this.apiHeader = apiHeader;
        this.setAuthenticationManager(new ProviderManager(provider));
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(apiHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}
