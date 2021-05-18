package com.ably.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LogoutHandler extends SecurityContextLogoutHandler {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       Authentication authentication) {

        super.logout(httpServletRequest, httpServletResponse, authentication);

        String issuer = (String) getClientRegistration().getProviderDetails().getConfigurationMetadata().get("issuer");
        String clientId = getClientRegistration().getClientId();
        String logoutUrl = UriComponentsBuilder
                .fromHttpUrl(issuer + "v2/logout?client_id={clientId}")
                .encode()
                .buildAndExpand(clientId)
                .toUriString();

        try {
            httpServletResponse.sendRedirect(logoutUrl);
        } catch (IOException ignored) { }
    }

    private ClientRegistration getClientRegistration() {
        return this.clientRegistrationRepository.findByRegistrationId("auth0");
    }
}
