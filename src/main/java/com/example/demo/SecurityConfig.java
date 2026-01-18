package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    DataSource dataSource;

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
        http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/signin").permitAll()
                        .anyRequest().authenticated());
//     http.httpBasic(Customizer.withDefaults());
     http.addFilterBefore(authTokenFilter,
             UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1= User.withUsername("user1")
//              .password("{noop}password")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        UserDetails admin=User.withUsername("admin")
           //     .password("{noop}admin0password")
                .password(passwordEncoder().encode("admin0password"))
                .roles( "ADMIN")
                .build();
        UserDetails user2=User.withUsername("user2")
//                .password("{noop}user2password")
                .password(passwordEncoder().encode("user2password"))
                .roles("USER")
                .build();

        UserDetails user3=User.withUsername("user3")
//                .password("{noop}user3password")
                .password(passwordEncoder().encode("user3password"))
                .roles("USER")
                .build();


       // return new InMemoryUserDetailsManager(user1,admin,admin1,admin2);
        JdbcUserDetailsManager userDetailsManager=new
                JdbcUserDetailsManager(dataSource);
        if(!userDetailsManager.userExists(user1.getUsername())){
            userDetailsManager.createUser(user1);

        }
        if(!userDetailsManager.userExists(user2.getUsername())){
            userDetailsManager.createUser(user2);

        }        if(!userDetailsManager.userExists(user3.getUsername())){
            userDetailsManager.createUser(user3);

        }        if(!userDetailsManager.userExists(admin.getUsername())){
            userDetailsManager.createUser(admin);

        }
       return userDetailsManager;


    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder){
        return builder.getAuthenticationManager();
     }


}
