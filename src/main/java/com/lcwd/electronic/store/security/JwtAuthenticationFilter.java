package com.lcwd.electronic.store.security;


import com.lcwd.electronic.store.util.AppConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtToken jwtToken;
    private final UserDetailsService userDetailsService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getServletPath();
        for (String publicUrl : AppConstant.PUBLIC_URLS) {
            if (publicUrl.equals("/**")) {
                return true;
            }
            if (publicUrl.endsWith("/**")) {
                String pattern = publicUrl.substring(0, publicUrl.length() - 2);
                if (requestPath.startsWith(pattern)) {
                    log.debug("Skipping JWT filter for public URL: {}", requestPath);
                    return true;
                }
            } else if (requestPath.equals(publicUrl)) {
                log.debug("Skipping JWT filter for public URL: {}", requestPath);
                return true;
            }
        }
        return false;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtToken.extractEmail(token);


            if (jwtToken.validateToken(token) && jwtToken.isAccessToken(token)) {



                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }


            }


        }

        filterChain.doFilter(request, response);


    }
}