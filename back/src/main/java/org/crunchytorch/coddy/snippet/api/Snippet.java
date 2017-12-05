package org.crunchytorch.coddy.snippet.api;

import org.crunchytorch.coddy.snippet.elasticsearch.entity.SnippetEntity;
import org.crunchytorch.coddy.snippet.service.SnippetService;
import org.crunchytorch.coddy.user.data.security.Permission;
import org.crunchytorch.coddy.user.filter.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Component
@Path("/snippet")
public class Snippet {

    @Autowired
    private SnippetService snippetService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SnippetEntity> getSnippets(@DefaultValue("0") @QueryParam("from") final int from,
                                           @DefaultValue("10") @QueryParam("size") final int size) {
        return snippetService.getEntity(from, size);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AuthorizationFilter
    public SnippetEntity create(@Context SecurityContext securityContext, SnippetEntity snippet) {
        return snippetService.create(snippet, securityContext);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public SnippetEntity getSnippet(@PathParam("id") String id) {
        return snippetService.getSnippet(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @AuthorizationFilter
    @RolesAllowed({Permission.ADMIN, Permission.PERSO_SNIPPET})
    public SnippetEntity updateSnippet(@PathParam("id") String id, SnippetEntity snippet) {
        snippet.setId(id);
        return this.snippetService.update(snippet);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @AuthorizationFilter
    @RolesAllowed({Permission.ADMIN, Permission.PERSO_SNIPPET})
    public void delete(@PathParam("id") String id) {
        snippetService.delete(id);
    }
}
