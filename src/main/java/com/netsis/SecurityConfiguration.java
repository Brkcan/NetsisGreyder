package com.netsis;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	 @Autowired
	    public void WebSecurityConfig(UserDetailsService userDetailsService) {
	        this.userDetailsService = userDetailsService;
	    }

	//master
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username,password,enabled from tblProjects where username=?")
		.authoritiesByUsernameQuery("select username, authority " + "from authorities where username=?");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.authorizeRequests().antMatchers("/*favicon.ico", "/css/**", "/js/**", "/images/**", "/webjars/**")
		//.permitAll();
		//http.authorizeRequests().anyRequest().authenticated();
		http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
	}
	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
}
