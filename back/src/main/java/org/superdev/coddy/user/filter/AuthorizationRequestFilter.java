package org.superdev.coddy.user.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.superdev.coddy.user.data.JWTPrincipal;
import org.superdev.coddy.user.service.JWTService;
import org.superdev.coddy.user.utils.JWTUtils;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@AuthorizationFilter
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationRequestFilter.class);


    @Context
    private HttpServletRequest request;

    @Autowired
    private JWTService jwtService;

    /**
     * Text that prefix header content (W3C convention), a.k.a. Bearer
     */
    private static final String HEADER_PREFIX = "Bearer ";

    /**
     * This method will catch any request and will analyse the header value of "Authorization" key.
     * If the key is valid, then it will extract the permission user from the token (see {@link JWTService#validateToken(String)}  validateToken()})
     * and put in a Jwt Security Context. see : {@link JWTSecurityContext}
     *
     * @param requestContext : the request context
     * @throws IOException            if an I/O exception occurs.
     * @throws NotAuthorizedException : if the request doesn't contain the token in the header,
     *                                then the user is not authenticated and not allowed to access to the application
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.debug("enter in jwt token interceptor");

        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (token == null) {
            throw new NotAuthorizedException("user is not authenticated");
        }

        if (token.startsWith(AuthorizationRequestFilter.HEADER_PREFIX)) {
            // Remove header prefix
            token = token.substring(AuthorizationRequestFilter.HEADER_PREFIX.length());
        }

        // if the token is valid, jwt returns an object Principal which contains the list of the user permissions
        JWTPrincipal principal = this.jwtService.validateToken(token);

        String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
        requestContext.setSecurityContext(new JWTSecurityContext(principal, scheme));
        requestContext.setProperty(JWTUtils.RequestProperty.PRINCIPAL.getProperty(), principal);
        // set Principal inside the contact in order to be retrieved by the LoggerAspect
        request.setAttribute(JWTUtils.RequestProperty.PRINCIPAL.getProperty(), principal);
    }
}
