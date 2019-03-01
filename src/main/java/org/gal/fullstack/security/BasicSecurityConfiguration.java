package org.gal.fullstack.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, role_ from user_role where username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		String[] permittedUris = { "/api/auth/authenticate", "/api/users/register", "/", "/*.js", "/*.js.map", "/*.html",
//				"/*.woff", "/*.woff2", "/*.svg", "/*.eot", "/*.ico", "/*.txt" };
		
//		http
//				.csrf().disable()
//				.authorizeRequests()
//				.antMatchers(permittedUris).permitAll()
//				.anyRequest()
//				.authenticated()
//				.and()
//				.httpBasic();
		http
		.csrf().disable()
        .authorizeRequests()
//        	.antMatchers("/**", "/api/auth/authenticate", "/api/users/register").permitAll()
        	//.antMatchers("/api/auth/authenticate", "/api/users/register").permitAll()
        	.antMatchers("/api/auth/authenticate", "/api/users/register").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/**").permitAll()
            .and()
            .httpBasic();
	}

}
