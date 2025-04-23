package sia.plants.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthFilter extends GenericFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Found cookie: " + cookie.getName());
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtService.validateToken(token)) {
                        String userId = jwtService.extractUserId(token);
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userId, null, null);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        break;
                    }
                }
            }
        }

        chain.doFilter(req, res);
    }
}
