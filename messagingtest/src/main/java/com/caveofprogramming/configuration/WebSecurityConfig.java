package com.caveofprogramming.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.caveofprogramming.service.UserService;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 5)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @formatter:off
		
		http
			.headers()
				.frameOptions()
				.sameOrigin()
			.and()
				.authorizeRequests()
			//.csrf()
        	//	.ignoringAntMatchers("/chat/**")			
				.antMatchers(
						"/",
						"/tos",
						"/favicon.ico",
						"/search",
						"/about",
						"/register",
						"/registrationconfirmed",
						"/invaliduser",
						"/expiredtoken",
						"/verifyemail",
						"/confirmregister",
						"/profilephoto/*",
						"/sessiontimeout"
						)
					.permitAll()
				.antMatchers(
					"/js/*",
					"/css/*",
					"/img/*")
					.permitAll()
				.antMatchers("/addstatus",
						"/editstatus",
						"/deletestatus",
						"/viewstatus")
					.hasRole("ADMIN")
				.antMatchers(
						"/stayloggedin",
						"/profile",
						"/profile/*",
						"/edit-profile-about",
						"/upload-profile-photo",
						"/save-interest",
						"/delete-interest",
						"/delete-account",
						"/chatview/**",
						"/messages",
						"/queue/**",
						"/app/**",
						"/chat/**",
						"/messagecount",
						"/conversation/*"
						)
					.authenticated()
				.anyRequest()
					.denyAll()
				.and()
				.sessionManagement()
					.maximumSessions(1)
	                .expiredUrl("/login?expired")
	                .and()
	                .invalidSessionUrl("/login")
	                .sessionFixation().newSession()
	            .and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/", true)
					.permitAll()
					.authenticationDetailsSource(authenticationDetailsSource())
					.and()
				.logout()
					.clearAuthentication(true)
					.logoutSuccessUrl("/login")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID");
		
		// @formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		// Ensure security registry is notified if sessions ends.
	    return new HttpSessionEventPublisher();
	}
	
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource() {

        return new AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>() {

            @Override
            public WebAuthenticationDetails buildDetails(
                    HttpServletRequest request) {
            	
                return new CustomWebAuthenticationDetails(request);
            }

        };
    }

}
