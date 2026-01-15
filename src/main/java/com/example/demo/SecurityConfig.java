package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        return http.build();

    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1= User.withUsername("user1")
                .password("{noop}password")
                .roles("USER")
                .build();
        UserDetails admin=User.withUsername("admin")
                .password("{noop}admin0password")
                .roles( "ADMIN")
                .build();
        UserDetails admin1=User.withUsername("user2")
                .password("{noop}user2password")
                .roles("USER")
                .build();

        UserDetails admin2=User.withUsername("user3")
                .password("{noop}user3password")
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(user1,admin,admin1,admin2);
    }


}
