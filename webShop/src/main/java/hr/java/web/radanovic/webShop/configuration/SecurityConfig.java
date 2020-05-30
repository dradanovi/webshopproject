package hr.java.web.radanovic.webShop.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;

	/**
	 * authorization of the login user name and password using the connected
	 * database as a referance
	 */
	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(datasource)
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT u.USERNAME, aut.AUTHORITY" + " FROM authorities aut"
						+ " JOIN users_authority ua" + " ON aut.ID=ua.AUTHORITY_ID" + " JOIN users u"
						+ " ON u.ID=ua.USERS_ID" + " WHERE u.username = ?")
				.passwordEncoder(passwordEncoder());
	}

	/**
	 * sets the actions that are performed on mapping for failure and success
	 */
	@Override
	public void configure(final HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/anonymous*")
//				.anonymous().antMatchers("/login*").permitAll().anyRequest().authenticated().and().formLogin()
//				.loginPage("/login.html").loginProcessingUrl("/perform_login").defaultSuccessUrl("homepage.html", true)
//				.failureUrl("/login.html?error=true").and().logout().logoutUrl("/perform_logout")
//				.deleteCookies("JSESSIONID").logoutSuccessUrl("/login.html");

		http.formLogin().loginPage("/login").defaultSuccessUrl("/success").failureUrl("/login-error").and().logout()
				.logoutSuccessUrl("/login");
	}

	/**
	 * returns the object for encoding the raw password for the login method
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
