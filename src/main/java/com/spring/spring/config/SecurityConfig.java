package com.spring.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.spring.entity.Utilisateur;
import com.spring.spring.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://**.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ 1. ROUTES PUBLICLES SPÉCIFIQUES (DONS)
                        .requestMatchers(HttpMethod.POST, "/api/dons/intention").permitAll()
                        .requestMatchers("/api/dons/confirmation/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // ✅ 2. ROUTES D'AUTHENTIFICATION PUBLIQUES
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/check").permitAll()
                        .requestMatchers("/api/auth/check-session").permitAll()
                        .requestMatchers("/api/auth/test-login").permitAll()

                        // ✅ 3. ROUTES GET PUBLIQUES (tout le monde peut lire)
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

                        // ✅ 4. ROUTES ADMIN SPÉCIFIQUES
                        .requestMatchers("/api/admin/dons/**").hasAnyRole("ADMIN", "EDITEUR")
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")
                        .requestMatchers("/api/auth/users/**").hasRole("ADMIN")

                        // ✅ 5. ROUTES AUTHENTIFIÉES (utilisateurs connectés)
                        .requestMatchers("/api/auth/change-password").authenticated()
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/auth/profile").authenticated()

                        // ✅ 6. ROUTES PROTÉGÉES (POST/PUT/DELETE)
                        .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        // ✅ 7. TOUT LE RESTE NÉCESSITE AUTH
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("motDePasse")
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            // Récupérer l'utilisateur complet
                            String email = authentication.getName();
                            Utilisateur utilisateur = customUserDetailsService.loadUserComplete(email);

                            // Créer la réponse JSON
                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("success", true);
                            responseBody.put("message", "Connexion réussie");
                            responseBody.put("user", createUserMap(utilisateur));

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = mapper.writeValueAsString(responseBody);
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("success", false);
                            responseBody.put("message", "Email ou mot de passe incorrect");
                            responseBody.put("error", exception.getMessage());

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = mapper.writeValueAsString(responseBody);
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("success", true);
                            responseBody.put("message", "Déconnexion réussie");

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = mapper.writeValueAsString(responseBody);
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("success", false);
                            responseBody.put("authenticated", false);
                            responseBody.put("message", "Authentification requise");

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = mapper.writeValueAsString(responseBody);
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("success", false);
                            responseBody.put("message", "Accès refusé");

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = mapper.writeValueAsString(responseBody);
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                );

        return http.build();
    }

    // Méthode utilitaire pour créer un Map sécurisé de l'utilisateur
    private Map<String, Object> createUserMap(Utilisateur utilisateur) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", utilisateur.getId() != null ? utilisateur.getId() : 0);
        userMap.put("nom", utilisateur.getNom() != null ? utilisateur.getNom() : "");
        userMap.put("email", utilisateur.getEmail() != null ? utilisateur.getEmail() : "");
        userMap.put("role", utilisateur.getRole() != null ? utilisateur.getRole() : "EDITEUR");
        userMap.put("photoUrl", utilisateur.getPhotoUrl() != null ? utilisateur.getPhotoUrl() : "");
        userMap.put("actif", utilisateur.getActif() != null ? utilisateur.getActif() : false);
        return userMap;
    }
}