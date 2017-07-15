package org.superdev.coddy.user.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.superdev.coddy.user.data.JWTPrincipal;
import org.superdev.coddy.user.data.Token;
import org.superdev.coddy.user.service.JWTService;
import org.superdev.coddy.user.utils.JWTUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class TokenResponseFilter implements ContainerResponseFilter {


    @Autowired
    private JWTService jwtService;

    /**
     * Filter method called after a response has been provided for a request.
     * This method will refresh the session of the current token and put in the header with the key Authorization
     *
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        JWTPrincipal principal = (JWTPrincipal) requestContext.getProperty(JWTUtils.RequestProperty.PRINCIPAL.getProperty());

        if (principal != null) {
            responseContext.getHeaders().add(HttpHeaders.AUTHORIZATION, this.jwtService.generateToken(principal).getToken());
        }
    }

}
