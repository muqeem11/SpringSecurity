package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
//    @Autowired
//    private UserDetailsService userDetailsService;
    @Autowired
     private JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("AuthTokenFilter called");
        try{
            String jwt =parseJwt(request);
            if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
                System.out.println("Token here :"+jwt);
                String username= jwtUtils.getUsernameFromToken(jwt);
//                 UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new
                        UsernamePasswordAuthenticationToken
                        (username, null, List.of());
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        filterChain.doFilter(request,response);
    }
    private String parseJwt(HttpServletRequest request){
        String jwt=jwtUtils.getJwtFromHeader(request);
        return jwt;

    }
}

