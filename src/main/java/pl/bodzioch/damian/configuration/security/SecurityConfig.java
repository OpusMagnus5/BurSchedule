package pl.bodzioch.damian.configuration.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import pl.bodzioch.damian.dao.UserDAO;
import pl.bodzioch.damian.model.UserModel;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDAO userDAO;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/app/login").permitAll()
                        .requestMatchers("/admin/**", "app/login/**").hasRole(UserRoles.ADMIN.getRoleCode())
                        .requestMatchers("/scheduler/**", "/schedulercreate/**", "schedulerupload/**", "/services/**",
                                "/templates/**", "/util/**", "/login/**").permitAll()
                        .anyRequest().hasRole(UserRoles.USER.getRoleCode()))
                .securityContext(securityContext -> securityContext.requireExplicitSave(true)
                        .securityContextRepository(new HttpSessionSecurityContextRepository()))
                .sessionManagement(session -> session.maximumSessions(1))
                .logout(logout -> logout.logoutUrl("/app/logout"))
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/services-list")
                        .failureUrl("/login"));

        return http.build();
    }

    @Bean
    @DependsOn(value = {"passwordEncoder", "userDetailsService"})
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserModel user = userDAO.getUserByUsername(username);
            return UserDetailsModel.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .build(); //TODO rzucanie wyjątku gdy user sie nie znajdzie
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
