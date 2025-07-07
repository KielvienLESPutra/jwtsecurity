package kielvien.lourensius.ekasetiaputra.jwtsecurity.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.TokenExpiredException;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.services.JWTServices;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final HandlerExceptionResolver handlerExceptionResolver;

	private final JWTServices jwtService;

	public JwtAuthenticationFilter(JWTServices jwtService, HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtService = jwtService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);
		try {
			Claims claims = jwtService.decodeJwt(token);
			String username = claims.getSubject();
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				Map<String, Object> userKeys = (Map<String, Object>) claims.get("userKeys");

				List<GrantedAuthority> authorities = ((List<?>) userKeys.get("permissionName")).stream()
						.map(Object::toString).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
						authorities);

				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
				filterChain.doFilter(request, response);
			}
		} catch (ExpiredJwtException exception) {
			exception.printStackTrace();
			handlerExceptionResolver.resolveException(request, response, null,
					new TokenExpiredException("Expired JWT Token"));
		} catch (JwtException exception) {
			exception.printStackTrace();
			handlerExceptionResolver.resolveException(request, response, null,
					new TokenExpiredException("Invalid JWT Token"));
		} catch (Exception exception) {
			exception.printStackTrace();
			handlerExceptionResolver.resolveException(request, response, null,
					new TokenExpiredException("General Error JWT Token"));
		}
	}

}
