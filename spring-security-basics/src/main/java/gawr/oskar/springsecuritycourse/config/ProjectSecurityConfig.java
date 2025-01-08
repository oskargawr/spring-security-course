package gawr.oskar.springsecuritycourse.config;

import gawr.oskar.springsecuritycourse.exception.CustomAccessDeniedHandler;
import gawr.oskar.springsecuritycourse.exception.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(smc -> smc.sessionFixation().none()) // not recommended
//                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(1).max) // for mvc paths
//                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // https only
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error", "/register").permitAll());
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(hxc -> hxc.accessDeniedHandler(new CustomAccessDeniedHandler()));
//        http.exceptionHandling(hxc -> hxc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied")); // only really for mvc apps, no use for this if using spring only for rest

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // if you dont give a prefix in the password it will use BCrypt (check docs)
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker(); // block compromised passwords
//    }

}