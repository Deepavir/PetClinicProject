package org.springframework.samples.petclinic.securityconfig;

import io.jsonwebtoken.*;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.samples.petclinic.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

		public final long JWT_TOKEN_VALIDITY = 5 * 60*60;
		
		 private String SECRET_KEY = "jwtTokenKey";

		    public String extractUsername(String token) {
		        return extractClaim(token, Claims::getSubject);
		    }

		    public Date extractExpiration(String token) {
		        return extractClaim(token, Claims::getExpiration);
		    }

		    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		        final Claims claims = extractAllClaims(token);
		        return claimsResolver.apply(claims);
		    }
		    private Claims extractAllClaims(String token) {
		        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		    }

		    private Boolean isTokenExpired(String token) {
		        return extractExpiration(token).before(new Date());
		    }
	//this will generate token
		    public String generateToken(UserDetails userDetails) {
		    	System.out.println("generateToken started");
		        Map<String, Object> claims = new HashMap<>();
		        return createToken(claims, userDetails.getUsername());
		    }

		    private String createToken(Map<String, Object> claims, String subject) {
		    	
		    	System.out.println("createToken started");
		    	System.out.println("claims  " + claims);
		    	System.out.println("subject " + subject);
		        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
		                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
		                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		    }

		    public Boolean validateToken(String token, UserDetails userDetails) {
		        final String username = extractUsername(token);
		        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		    }
		}	
	