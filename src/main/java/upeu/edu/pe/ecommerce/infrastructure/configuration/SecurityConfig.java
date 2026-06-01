/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upeu.edu.pe.ecommerce.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import upeu.edu.pe.ecommerce.infrastructure.services.UserDetailServiceImpl;
import upeu.edu.pe.ecommerce.infrastructure.services.loginHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final UserDetailServiceImpl userDetailService;
    private final Environment environment;
    
    @Autowired
    private loginHandler loginHandler;
    

    public SecurityConfig(UserDetailServiceImpl userDetailService, Environment environment) {
        this.userDetailService = userDetailService;
        this.environment = environment;
    }
     //metodo de autentificación
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
      @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        boolean securityEnabled = environment.getProperty("spring.security.enabled", Boolean.class, true);
        if (!securityEnabled) {
            return httpSecurity.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authRequest -> authRequest.anyRequest().permitAll())
                    .build();
        }
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER").anyRequest().permitAll())
                .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginHandler))
                .logout(log -> log
                .logoutSuccessUrl("/close"))
                .build();
    }
    
    
    //metodo para encriptar las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
}
