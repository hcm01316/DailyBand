package com.bnd.dailyband.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.bnd.dailyband.security.CustomAccessDeniedHandler;
import com.bnd.dailyband.security.CustomUserDetailsService;
import com.bnd.dailyband.security.LoginFailHandler;
import com.bnd.dailyband.security.LoginSuccessHandler;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	private DataSource dataSource;
	private LoginFailHandler loginFailHandler;
	private LoginSuccessHandler loginSuccessHandler;
	private CustomUserDetailsService customUserDetailsService;
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	@Autowired
	public SecurityConfig(LoginFailHandler loginFailHandler,
						  LoginSuccessHandler loginSuccessHandler,
						  CustomUserDetailsService customUserDetailsService,
						  DataSource dataSource,
						  CustomAccessDeniedHandler customAccessDeniedHandler) {
		this.loginFailHandler = loginFailHandler;
		this.loginSuccessHandler = loginSuccessHandler;
		this.customUserDetailsService = customUserDetailsService;
		this.dataSource = dataSource;
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

		http.authorizeHttpRequests(
				(au)-> au
							.requestMatchers( "/member/list", "/member/info", "/member/delete")
											.hasAuthority("ROLE_ADMIN")
							.requestMatchers( "/member/update")
											.hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
							.requestMatchers( "/board/**", "/comment/**")
											.hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
											.requestMatchers( "/**").permitAll()
				);

		 http.formLogin((fo) -> fo.loginPage("/member/login")
		 			.loginProcessingUrl("/member/loginProcess")
		 			.usernameParameter("id")
		 			.passwordParameter("pass")
		 			.successHandler(loginSuccessHandler)
		 			.failureHandler(loginFailHandler));
		 			

		 	http.logout((lo) -> lo.logoutSuccessUrl("/main")
		 						  .logoutUrl("/member/logout")
		 						  .invalidateHttpSession(true)
		 						  .deleteCookies("saveid", "JSESSION_ID"));
		 

//		 	http.rememberMe((me) -> me.rememberMeParameter("remember-me")
//		 							  .userDetailsService(customUserDetailsService)
//		 							  .tokenValiditySeconds(2419200)
//		 							  .tokenRepository(tokenRepository()));
		 	
		 	http.exceptionHandling((ex)-> ex.accessDeniedHandler(customAccessDeniedHandler));


		 return http.build();
	}



	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource); // import javax.sql.DataSource
		return jdbcTokenRepository;
	}
	

}
