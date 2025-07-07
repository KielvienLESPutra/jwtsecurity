package kielvien.lourensius.ekasetiaputra.jwtsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.configuration.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		Authentication authtest = SecurityContextHolder.getContext().getAuthentication();
		log.info("Auth info = {}", authtest);
		if (authtest != null) {
			authtest.getAuthorities().forEach(grantedAuthority -> 
		        log.info("Authority: {}", grantedAuthority.getAuthority())
		    );
		}
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
//            	.requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
//                .requestMatchers("/api/user/signUp").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
