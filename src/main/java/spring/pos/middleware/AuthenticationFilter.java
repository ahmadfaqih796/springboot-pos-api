package spring.pos.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import spring.pos.security.JwtAuthenticationToken;
import spring.pos.util.JwtUtil;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

   private final JwtUtil jwtUtil;
   private final UserDetailsService userDetailsService;
   private final ObjectMapper objectMapper;

   public AuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
      this.jwtUtil = jwtUtil;
      this.userDetailsService = userDetailsService;
      this.objectMapper = objectMapper;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
         throws ServletException, IOException {
      String authHeader = request.getHeader("Authorization");
      String username = null;
      String jwt = null;

      // untuk mengakses path tanpa token
      if (request.getRequestURI().startsWith("/swagger-ui/")
            ||
            request.getRequestURI().startsWith("/v3/api-docs/")) {
         chain.doFilter(request, response);
         return;
      }

      try {
         if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.getUsernameFromToken(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               var userDetails = this.userDetailsService.loadUserByUsername(username);

               if (jwtUtil.validateToken(jwt)) {
                  var authToken = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
               }
            }
         }
      } catch (SignatureException ex) {
         // response untuk kalau token invalid / belum login
         response.setContentType("application/json");
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         Map<String, Object> errorResponse = Map.of(
               "timestamp", LocalDateTime.now(),
               "status", HttpServletResponse.SC_UNAUTHORIZED,
               "error", "Invalid JWT Signature",
               // "message", ex.getMessage(),
               "message", "Silahkan login terlebih dahulu",
               "path", request.getRequestURI());
         response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
         return;
      } catch (ExpiredJwtException ex) {
         // response untuk kalau token expired
         response.setContentType("application/json");
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         Map<String, Object> errorResponse = Map.of(
               "timestamp", LocalDateTime.now(),
               "status", HttpServletResponse.SC_UNAUTHORIZED,
               "error", "Token expired",
               // "message", ex.getMessage(),
               "message", "maaf session kamu habis, silahkan login",
               "path", request.getRequestURI());
         response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
         return;
      }

      chain.doFilter(request, response);
   }
}