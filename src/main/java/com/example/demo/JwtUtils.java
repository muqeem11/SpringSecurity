package com.example.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

import java.util.Date;



@Component
public class JwtUtils {
//    @Autowired
//    private  UserDetailsManager userDetailsManager;
    private String jwtSecret ="YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=";
    private int jwtExpirationInMs =172800000;//48hrs

//    public JwtUtils(UserDetailsManager userDetailsManager) {
//        this.userDetailsManager = userDetailsManager;
//    }

    public String getJwtFromHeader(HttpServletRequest request){
       String bearerToken=request.getHeader("Authorization");
       if(bearerToken!=null&&bearerToken.startsWith("Bearer "))
          return bearerToken.substring(7);
       return null;
   }
   public String generateTokenFromUsername(UserDetails userDetails){
       String userName=userDetails.getUsername();

       return Jwts.builder()
               .subject(userName)
               .claim("roles",userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
               .issuedAt(new Date())
               .expiration(new Date(new Date().getTime()+jwtExpirationInMs))
               .signWith(key())
               .compact();
   }

    public Boolean validateJwtToken(String jwtToken) {
        return true;
    }
    private Key key(){
       return Keys.hmacShaKeyFor((Decoders.BASE64.decode(jwtSecret)));
    }

    public String getUsernameFromToken(String jwt) {
       return Jwts.parser().verifyWith((SecretKey) key())
               .build().parseSignedClaims(jwt)
               .getPayload().getSubject();
    }

    public Claims getAllClaims(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(jwt)
                .getPayload();
    }
}
