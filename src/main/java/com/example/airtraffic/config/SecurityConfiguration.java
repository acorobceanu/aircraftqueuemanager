package com.example.airtraffic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// TODO enable security
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/aircraft/**").hasRole("USER");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr
                .inMemoryAuthentication()
                .withUser("admin").password("admin").authorities("ADMIN", "USER")
                .and()
                .withUser("user").password("user").authorities("USER");
    }
}
