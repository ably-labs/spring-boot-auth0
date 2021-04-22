package com.ably.chat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.*;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.rest.Auth;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Capability;
import net.minidev.json.JSONArray;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AblyController
{
    private AblyRest ablyRest;

    @Value( "${ABLY_API_KEY}" )
    private void setAblyRest(String apiKey) throws AblyException {
        ablyRest = new AblyRest(apiKey);
    }

    @RequestMapping("/ably-token")
    public String auth(final Authentication authentication, HttpServletResponse response) throws AblyException
    {
        System.out.println(authentication);
        Map<String,Object> attributes = new HashMap<String, Object>();
        if (authentication != null) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = token.getPrincipal();
            attributes = oAuth2User.getAttributes();
        }


        Auth.TokenParams tokenParams = getTokenParams(attributes);
        return createTokenRequest(tokenParams, response);
    }

    public Auth.TokenParams getTokenParams(Map<String, Object> attributes) throws AblyException
    {
        String username = "Anonymous";
        String capability = "{ '*': ['subscribe'] }";
        if (attributes.containsKey("name")) {
            username = (String) attributes.get("name");
            capability = "{ '*': ['subscribe', 'publish', 'presence'] }";
        }

        Auth.TokenParams tokenParams = new Auth.TokenParams();
        tokenParams.capability = Capability.c14n(capability);
        tokenParams.clientId = username;
        return tokenParams;
    }

    public String createTokenRequest(Auth.TokenParams tokenParams, HttpServletResponse response) {
        Auth.TokenRequest tokenRequest;
        try {
            tokenRequest = ablyRest.auth.createTokenRequest(tokenParams, null);
            response.setHeader("Content-Type", "application/json");
            return tokenRequest.asJson();
        } catch (AblyException e) {
            response.setStatus(500);
            return "Error requesting token: " + e.getMessage();
        }
    }
}
