package com.example.chat.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.chat.service.UserDetailServiceImp;
import com.example.chat.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter  {

    private final JwtUtil jwtUtil;
    private final UserDetailServiceImp userDetailService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailServiceImp userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                 filterChain.doFilter(request, response);
                return;
            }
            
            String authHeader=request.getHeader("Authorization");
            String token=null;
            String username=null;

            if(authHeader != null && authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);
                try{
                    username=jwtUtil.extractUsername(token);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=userDetailService.loadUserByUsername(username);

                if(jwtUtil.validateToken(token, username)){
                    UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken( userDetails,null, Collections.emptyList());
                    authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authtoken);
                }
            }
            filterChain.doFilter(request, response);
    }

}
