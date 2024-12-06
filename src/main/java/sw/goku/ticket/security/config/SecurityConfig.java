package sw.goku.ticket.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import sw.goku.ticket.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sw.goku.ticket.security.repository.user.Permission;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and()
                .csrf(csrfConfig -> csrfConfig.disable())  // Desactiva CSRF para JWT
                .sessionManagement(sessionMangConfig ->
                        sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sesiones sin estado
                .authenticationProvider(authProvider) // Proveedor de autenticación personalizado
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Filtro JWT
                .authorizeHttpRequests(authConfig -> {

                    // Endpoints públicos
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/auth/public-access").permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                    // Endpoints protegidos según permisos
                    authConfig.requestMatchers(HttpMethod.POST, "/api/tickets")
                            .hasAuthority(Permission.CREATE_TICKETS.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/api/tickets/all-ticket")
                            .hasAuthority(Permission.READ_ALL_TICKETS.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/api/tickets/all-my-ticket")
                            .hasAuthority(Permission.LIST_YOUR_TICKETS.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/api/tickets/{ticketId}")
                            .hasAuthority(Permission.READ_ALL_TICKETS.name());
                    authConfig.requestMatchers(HttpMethod.PUT, "/api/tickets/{ticketId}")
                            .hasAuthority(Permission.UPDATE_TICKETS.name());
                    authConfig.requestMatchers(HttpMethod.DELETE, "/api/tickets/{ticketId}")
                            .hasAuthority(Permission.CANCEL_TICKETS.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/api/tickets/status")
                            .hasAuthority(Permission.TICKETS_STATUS.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/api/user")
                            .hasAuthority(Permission.GET_ROLE.name());

                    // Bloquear cualquier otra solicitud no configurada
                    authConfig.anyRequest().denyAll();
                });
        return http.build();
    }

    @Bean(name = "customCorsConfigurer")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));  // Asegúrate de que el origen sea correcto
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization")); // Permite cabeceras de contenido y autorización
        configuration.setAllowCredentials(true); // Permite el uso de credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
