    package com.pinjemFin.PinjemFin.security;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class JwtSecurityConfig {
        private final JwtFilter jwtFilter;

        public JwtSecurityConfig(JwtFilter jwtFilter) {
            this.jwtFilter = jwtFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(WebConfig.corsConfigurationSource()))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/login").permitAll() // Buka akses login
                            .requestMatchers("/auth/registerAkunCustomer").permitAll() // Buka akses register
                            .requestMatchers("/customer/forgot-password").permitAll()
                            .requestMatchers("/customer/reset-password").permitAll()
                            .requestMatchers("/customer/reset-password/**").permitAll()
                            .requestMatchers("/auth/loginEmployee").permitAll() // Buka akses login
                            .anyRequest().authenticated()  // Endpoint lain harus pakai token
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }
