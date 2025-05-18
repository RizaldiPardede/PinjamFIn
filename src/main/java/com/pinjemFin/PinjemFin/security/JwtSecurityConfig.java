    package com.pinjemFin.PinjemFin.security;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
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
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/auth/registerAkunCustomer").permitAll()
                            .requestMatchers("/customer/forgot-password").permitAll()
                            .requestMatchers("/customer/reset-password").permitAll()
                            .requestMatchers("/customer/reset-password/**").permitAll()
                            .requestMatchers("/auth/loginEmployee").permitAll()
                            .requestMatchers("/ws/**").permitAll()
                            .requestMatchers("/customer/getSimulasiNoAuth").permitAll()
                            .requestMatchers("/customer/cekEmailCustomer").permitAll()
                            .requestMatchers("/auth/loginWithgoogle").permitAll()
                            .requestMatchers("/auth/emailActivation").permitAll()
                            .requestMatchers("/auth//registerAuthGoogle").permitAll()// baru
                            .anyRequest().authenticated()  // Endpoint lain harus pakai token
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }
