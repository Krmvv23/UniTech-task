package az.unitech.unitech.configuration;

import az.unitech.unitech.custom.CustomUser;
import az.unitech.unitech.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/user","/api/user/login").permitAll()
                        .requestMatchers("/api/user/accounts","/api/account/**","/api/currency").hasRole("USER")
                ).httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return pin -> userService.findUserByPin(pin).map(user -> CustomUser.builder()
                .id(user.getId())
                .pin(user.getPin())
                .password(bCryptPasswordEncoder().encode(user.getPassword()))
                .role(user.getRole()).build()
        ).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
    }
}
