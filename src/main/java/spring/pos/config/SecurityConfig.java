package spring.pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import spring.pos.middleware.AuthenticationFilter;
import spring.pos.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final AuthenticationFilter jwtAuthenticationFilter;
   private final CustomUserDetailsService userDetailsService;

   public SecurityConfig(@Lazy AuthenticationFilter jwtAuthenticationFilter,
         @Lazy CustomUserDetailsService userDetailsService) {
      this.jwtAuthenticationFilter = jwtAuthenticationFilter;
      this.userDetailsService = userDetailsService;
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
         throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public UserDetailsService userDetailsService() {
      return userDetailsService;
   }

   @SuppressWarnings("deprecation")
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
            .csrf(csrf -> csrf.disable())
            .authorizeRequests(requests -> requests
                  .requestMatchers("/auth/**").permitAll() // untuk akses auth tanpa authorization
                  .anyRequest().authenticated())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

      http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }
}
