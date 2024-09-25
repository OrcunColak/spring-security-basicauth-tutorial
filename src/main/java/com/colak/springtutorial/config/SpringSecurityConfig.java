package com.colak.springtutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        customizer -> {
                            customizer.requestMatchers("/api/v1/secured/**")
                                    //.hasRole("ADMIN")
                                    .authenticated()
                                    .anyRequest()
                                    .permitAll();
                        })
                .httpBasic(Customizer.withDefaults());

        enableH2Console(http);

        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/secured/**")
                        // This is required for H2
                        .ignoringRequestMatchers("/h2-console/*")
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
                )
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.NEVER));

        return http.build();
    }

    // See https://stackoverflow.com/questions/53395200/h2-console-is-not-showing-in-browser
    private void enableH2Console(HttpSecurity http) throws Exception {
        // This allows the page to be displayed in a frame on the same origin as the page itself
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CompositeUserDetailsService();
    }

    @Bean
    public CustomerUserDetailsService customerUserDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean
    // This is not a bean. I am keeping it as reference only
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        String password = passwordEncoder().encode("password");

        UserDetails user = User.builder()
                .username("user")
                .password(password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // jUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?;");
        //
        // jUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?;");

        return jUserDetailsManager;
    }
}
