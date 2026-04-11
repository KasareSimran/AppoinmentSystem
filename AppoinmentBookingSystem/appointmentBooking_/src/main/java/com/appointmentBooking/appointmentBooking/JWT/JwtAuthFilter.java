package com.appointmentBooking.appointmentBooking.JWT;



import com.appointmentBooking.appointmentBooking.Service.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);


    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtProvider jwtProvider,
                         CustomUserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        try {
            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7);
                String phone = jwtProvider.extractUsername(token);

                if (phone != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    var userDetails = userDetailsService.loadUserByUsername(phone);

                    if (jwtProvider.validateToken(token)) {

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(auth);
//                        System.out.println("User: " + phone);
//                        System.out.println("Authorities: " + userDetails.getAuthorities());

                    }
                }
            }
        } catch (Exception e) {
            // 🔥 VERY IMPORTANT (avoid crash)
            logger.error("JWT Error: {}", e.getMessage(), e);

        }

        filterChain.doFilter(request, response);
    }

}
