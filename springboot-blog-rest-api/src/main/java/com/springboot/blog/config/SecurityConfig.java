package com.springboot.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	private UserDetailsService userDetailsService;
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	private JwtAuthenticationFilter authenticationFilter;
	
	
	public SecurityConfig(UserDetailsService userDetailsService,JwtAuthenticationEntryPoint authenticationEntryPoint,JwtAuthenticationFilter authenticationFilter) {
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint=authenticationEntryPoint;
		this.authenticationFilter=authenticationFilter;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
		}

	//We need password encoder bean as we are passing the password as plain text which wont work
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
//	 @Bean
//	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//	        return authenticationConfiguration.getAuthenticationManager();
//	    }
	
	private static final String[] AUTH_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**"
    };
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf)->csrf.disable())
				.authorizeHttpRequests((authorize)->
				//authorize.anyRequest().authenticated()
				authorize.requestMatchers(HttpMethod.GET,"/api/**").permitAll()
							.requestMatchers("/api/auth/**").permitAll()
//							.requestMatchers("/swagger-ui/**").permitAll()
//							.requestMatchers("/v3/api-docs/**").permitAll()
							.requestMatchers(AUTH_WHITELIST).permitAll()
							 .requestMatchers("/favicon.ico").permitAll() // Allow favicon
				             .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Allow static resources
							.anyRequest().authenticated()
						).exceptionHandling( exception -> exception
					.authenticationEntryPoint(authenticationEntryPoint)
					).sessionManagement(session->session
									.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
									);
				//.httpBasic(Customizer.withDefaults());
		http.addFilterBefore(authenticationFilter,UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails anjalee= User.builder().
//							username("anjalee")
//					 		.password(passwordEncoder().encode("Pass@12"))
//							.roles("USER")
//							.build();
//		
//		UserDetails admin=User.builder()
//						.username("admin")
//						.password(passwordEncoder().encode("admin"))
//						.roles("ADMIN")
//						.build();
//		
//		return new InMemoryUserDetailsManager(anjalee,admin);
//		
//	}

}
