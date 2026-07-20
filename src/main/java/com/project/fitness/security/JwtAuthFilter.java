package com.project.fitness.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(request);
            if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
                System.out.println("Token Here : " + jwt);

                String userId = jwtUtils.getUserIdFromToken(jwt);
                //UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Claims claims = jwtUtils.getAllClaims(jwt);
                System.out.println(claims);
                List<String> roles = claims.get("roles", List.class);
                System.out.println("ROLES"+roles);
                List<GrantedAuthority> authorities = List.of();
                if(roles!= null){
                    authorities = (List<GrantedAuthority>) roles.stream()
                            .map(role -> (GrantedAuthority)new SimpleGrantedAuthority(role)).toList();
                }
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
    public String parseJwt(HttpServletRequest request){
        String jwt = jwtUtils.getJwtFromHeader(request);
        return jwt;
    }
}
