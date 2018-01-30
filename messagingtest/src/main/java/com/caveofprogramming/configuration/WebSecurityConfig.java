package com.caveofprogramming.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.caveofprogramming.service.UserService;

@Configuration
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
		.csrf()
        	.ignoringAntMatchers("/chat/**")
        .and()
			.headers()
				.frameOptions()
				.sameOrigin()
			.and()
			.authorizeRequests()
				.antMatchers(
						"/",
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
						"/validsession",
						"/authenticated"
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
						"/profile",
						"/profile/*",
						"/edit-profile-about",
						"/upload-profile-photo",
						"/save-interest",
						"/delete-interest",
						"/chatview/*",
						"/messages",
						"/queue/**",
						"/app/**",
						"/chat/**",
						"/getchat/*"
						)
				.authenticated()
				.anyRequest()
				.denyAll()
				.and()
			.sessionManagement()
				.maximumSessions(3)
                .expiredUrl("/login?expired")
                .and()
                .invalidSessionUrl("/login")
                .and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.permitAll()
				.and()
			.logout()
				.permitAll()
				.logoutSuccessUrl("/login");
		
		// @formatter:on
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// @formatter:off
		auth.inMemoryAuthentication().withUser("john").password("hello").roles("USER");

		// @formatter:on

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}

}
