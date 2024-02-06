package com.colak.springsecuritybasicauthtutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        customizer -> {
                            customizer.requestMatchers(antMatcher("/h2-console/*")).permitAll();
                            customizer.requestMatchers("/api/v1/secured/**").authenticated();
                            customizer.anyRequest().permitAll();
                        })
                .httpBasic(Customizer.withDefaults());

        enableH2Console(http);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/secured/**")
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // This is not a bean. I am keeping it as reference only
    public InMemoryUserDetailsManager userDetailsService() {
        String password = passwordEncoder().encode("password");

        UserDetails user = User.builder()
                .username("user")
                .password(password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?;");

        jUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?;");

        return jUserDetailsManager;
    }
}
