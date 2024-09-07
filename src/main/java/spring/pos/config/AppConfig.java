package spring.pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.pos.util.JwtResponseHandler;

@Configuration
public class AppConfig {

   @Bean
   public JwtResponseHandler jwtResponseHandler() {
      return new JwtResponseHandler();
   }
}