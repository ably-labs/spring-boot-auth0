package com.ably.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final LogoutHandler logoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .oauth2Login()
            .and().logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .addLogoutHandler(logoutHandler);
    }
}
