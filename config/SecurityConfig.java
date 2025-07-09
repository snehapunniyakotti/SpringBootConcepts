package com.demo.authentication.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.session.web.http.CookieSerializer;
//import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.demo.authentication.controller.CustomAuthenticationSuccessHandler;
import com.demo.authentication.filter.JWTAuthFilter;
import com.demo.authentication.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JWTAuthFilter JWTAuthFilter;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManage(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authenticationProvider());
		return authenticationManagerBuilder.build();
	}

//	@Bean
//	public CookieSerializer cookieSerializer() {
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setCookieName("JSESSIONID");
//		serializer.setCookiePath("/");
//		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
//		return serializer;
//	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:5500/")); 																								// here
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all paths
		return source;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.addFilterBefore(JWTAuthFilter, UsernamePasswordAuthenticationFilter.class)
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(
						authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/authentication/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/thread/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
//   		             .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
//   		             .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER")
//   		             .requestMatchers(new AntPathRequestMatcher("/all/**")).hasAnyRole("ADMIN","USER")
								.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
						.ignoringRequestMatchers(new AntPathRequestMatcher("/authentication/**"))
						.ignoringRequestMatchers(new AntPathRequestMatcher("/thread/**"))
						.ignoringRequestMatchers(new AntPathRequestMatcher("/api/**"))
//   		             .ignoringRequestMatchers(new AntPathRequestMatcher("/admin/**"))
						.csrfTokenRepository(csrfTokenRepository()))

//   		         .headers(headers -> headers.frameOptions().sameOrigin()) 
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

//				.formLogin(form -> form.loginPage("/login").failureUrl("/login?error")
//						.successHandler(customAuthenticationSuccessHandler).permitAll())
				// .formLogin(form -> form.loginProcessingUrl("/login"))
//				 .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/index.html").permitAll())

				.build();
	}

//    @Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		http
// 			.authorizeHttpRequests((authorizeHttpRequests) ->
// 				authorizeHttpRequests
// 					.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
// 					.requestMatchers("/admin/**").hasRole("ADMIN")
// 					.requestMatchers("/user/**").hasRole("USER")
// 					.anyRequest().authenticated()
// 			)
// 			.csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/**")));
//// 			.formLogin(form -> form.loginProcessingUrl("/login"));
// 		return http.build();
// 	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin123"))
				.roles("ADMIN").build();
		UserDetails user = User.builder().username("user").password(passwordEncoder().encode("user123")).roles("USER")
				.build();
		return new InMemoryUserDetailsManager(admin, user);
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setSessionAttributeName("_csrf");
		return repository;
	}

}
