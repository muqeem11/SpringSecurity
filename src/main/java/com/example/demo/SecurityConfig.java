package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    DataSource dataSource;
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
        UserDetails user2=User.withUsername("user2")
                .password("{noop}user2password")
                .roles("USER")
                .build();

        UserDetails user3=User.withUsername("user3")
                .password("{noop}user3password")
                .roles("USER")
                .build();


       // return new InMemoryUserDetailsManager(user1,admin,admin1,admin2);
        JdbcUserDetailsManager userDetailsManager=new
                JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(user2);
        userDetailsManager.createUser(user3);
        userDetailsManager.createUser(admin);
       return userDetailsManager;


    }


}
