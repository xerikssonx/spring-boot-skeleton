package com.example;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.dao.UserDao;
import com.example.entity.User;
import com.sun.enterprise.module.bootstrap.Main;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * This section defines the user accounts which can be used for
	 * authentication as well as the roles each user has.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	protected UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				User user = userDao.findByUsername(username);
				if (user == null) {
					throw new UsernameNotFoundException("No user found with username: " + username);
				}

				Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority(user.getRole().getName().name()));

				return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
						true, true, true, true, authorities);
			}

		};
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/**
	 * This section defines the security policy for the app.
	 * <p>
	 * <ul>
	 * <li>BASIC authentication is supported (enough for this REST-based
	 * demo).</li>
	 * <li>/employees is secured using URL security shown below.</li>
	 * <li>CSRF headers are disabled since we are only testing the REST
	 * interface, not a web one.</li>
	 * </ul>
	 * NOTE: GET is not shown which defaults to permitted.
	 *
	 * @param http
	 * @throws Exception
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and().authorizeRequests().//
				antMatchers("/").hasRole("USER").and().//
				csrf().disable();
	}
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("edgar"));
	}
}