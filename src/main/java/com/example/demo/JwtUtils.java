package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;



@Component
public class JwtUtils {
    private String jwtSecret ="YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=";
    private int jwtExpirationInMs =172800000;//48hrs

   public String getJwtFromHeader(){
       return "";
   }
   public String generateTokenFromUsername(String username){

       return Jwts.builder()
               .subject(username)
               .issuedAt(new Date())
               .expiration(new Date(new Date().getTime()+jwtExpirationInMs))
               .signWith(key())
               .compact();
   }
    public Boolean validateJwtToken(){
       return true;
    }
    private Key key(){
       return Keys.hmacShaKeyFor((Decoders.BASE64.decode(jwtSecret)));
    }

}
