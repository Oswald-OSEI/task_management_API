package taskmanagement.taskmanagementapi.common.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import taskmanagement.taskmanagementapi.common.exceptions.InvalidAuthorizationHeaderException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    String secretString;

    private final TokenService tokenService;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(TokenService tokenService, CustomUserDetailsService customUserDetailsService) {
        this.tokenService = tokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {     
        try{
            String jwt = getJwtFromRequest(request);
                if (tokenService.validateToken(jwt, secretString).isValid()) {
                    UserDetails user = customUserDetailsService.loadUserByUsername(tokenService.extractUsername(jwt, secretString));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }else{
                    throw new InvalidAuthorizationHeaderException("token validation error");
                }

            }catch(Exception e){
                logger.error("Could not extract user from request", e);
                throw new InvalidAuthorizationHeaderException("token validation error");
        
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }else{
            return null;
        }
    }

     @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Add paths that should not be filtered
        return path.startsWith("v1/task-management/auth/") ||
               path.equals("/v1/task-management/auth/login") ||
               path.equals("/v1/task-management/user/sign-up")||
               path.equals("/v1/task-management/auth/token/refresh");
    }
}
