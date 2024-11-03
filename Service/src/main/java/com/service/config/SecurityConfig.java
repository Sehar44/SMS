package com.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.accessor.StudentAccessor;
import com.example.data.Admin;
import com.example.data.AdminRepository;
import com.example.data.Student;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AdminRepository adminRepository;
    private final StudentAccessor studentAccessor;

    public SecurityConfig(AdminRepository adminRepository, StudentAccessor studentAccessor) {
        this.adminRepository = adminRepository;
        this.studentAccessor = studentAccessor;
    }

    // Password encoder to encode and match passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
    	logger.info("PasswordEncoder bean created (BCryptPasswordEncoder)");
        return new BCryptPasswordEncoder();
    }

 
    // Defines custom UserDetailsService to handle logins
    @Bean
    public UserDetailsService userDetailsService() {
    	
    	logger.info("UserDetailsService bean created");
        return username -> {
        	 logger.info("Attempting to load user details for username: {}", username);
            if ("admin123".equals(username)) {
                Admin admin = adminRepository.findByUsername(username);
                if (admin == null) {
                	 logger.error("Admin not found with username: {}", username);
                    throw new UsernameNotFoundException("Admin not found");
                }
                else {
                	
                	logger.info("Admin found: {}", admin.getUsername());
                }
                return org.springframework.security.core.userdetails.User.withUsername(admin.getUsername())
                		 .password(admin.getPassword()) // Use the encoded password from the database
                         .authorities("ROLE_ADMIN") // Set role if necessary
                         .build();
            } else {
            Student student = studentAccessor.findByEmail(username);
            if (student != null && student.getIsActive() == 0) {
                
            	 logger.info("Student found: {}", student.getEmail());
                return org.springframework.security.core.userdetails.User.withUsername(student.getEmail())
                        .password(student.getPassword()) // Use the encoded password from the database
                        .authorities("ROLE_STUDENT") // Set role if necessary
                        .build();
            }
            
            logger.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found");
            }
        };
        
    }


    // DaoAuthenticationProvider to provide authentication
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	logger.info("DaoAuthenticationProvider bean created");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    //AuthenticationManager Bean to manage authentication providers
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    	logger.info("AuthenticationManager bean created");
        return authConfig.getAuthenticationManager();
    }

    //Configuring Security Filter Chain to define login and access rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	logger.info("SecurityFilterChain configuration started");
        http
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/students/studentHome","/students/Profile","/notices/studentNotice").hasRole("STUDENT")
				.requestMatchers("/admin/**","/students/all","/notices").hasRole("ADMIN")
				.requestMatchers("/uploads/**").permitAll() 
                .anyRequest().permitAll() // Allow all requests
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .permitAll()
                .defaultSuccessUrl("/default", true) // Redirect after successful login
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll()
                .logoutSuccessUrl("/login?breadcrumbs=") // Redirect to login after logout
                .invalidateHttpSession(true) // Invalidate session on logout
                .deleteCookies("JSESSIONID")
            )
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
            .sessionManagement(session -> session
                .invalidSessionUrl("/login") // Redirect invalid session to login page
                .maximumSessions(1) // Limit to one session per user
            );

        return http.build();
    }
}