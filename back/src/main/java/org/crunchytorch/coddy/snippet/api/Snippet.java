package org.crunchytorch.coddy.snippet.api;

import org.apache.commons.lang.StringUtils;
import org.crunchytorch.coddy.application.data.MediaType;
import org.crunchytorch.coddy.application.data.Page;
import org.crunchytorch.coddy.snippet.data.SearchBody;
import org.crunchytorch.coddy.snippet.elasticsearch.entity.SnippetEntity;
import org.crunchytorch.coddy.snippet.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/snippet", produces = MediaType.APPLICATION_JSON)
public class Snippet {

    @Autowired
    private SnippetService snippetService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<SnippetEntity> getSnippets(@RequestParam(value = "from", defaultValue = "0") final int from,
                                           @RequestParam(value = "size", defaultValue = "10") final int size,
                                           @RequestParam(value = "query", required = false) final String query) {
        if (StringUtils.isEmpty(query) || StringUtils.isEmpty(query.replaceAll("\\s+", ""))) {
            return snippetService.getEntity(from, size);
        }
        return snippetService.search(query, from, size);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON)
    public Page<SnippetEntity> search(@RequestParam(value = "from", defaultValue = "0") final int from,
                                      @RequestParam(value = "size", defaultValue = "10") final int size,
                                      @RequestBody SearchBody searchBody) {
        return snippetService.search(searchBody, from, size);
    }

    /*@POST
    @AuthorizationFilter
    public SnippetEntity create(@Context SecurityContext securityContext, SnippetEntity snippet) {
        return snippetService.create(snippet, securityContext);
    }*/

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public SnippetEntity getSnippet(@PathVariable("id") String id) {
        return snippetService.getSnippet(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON)
    //@AuthorizationFilter
    //@RolesAllowed({Permission.ADMIN, Permission.PERSO_SNIPPET})
    public SnippetEntity updateSnippet(@PathVariable("id") String id, SnippetEntity snippet) {
        snippet.setId(id);
        return this.snippetService.update(snippet);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    //@AuthorizationFilter
    //@RolesAllowed({Permission.ADMIN, Permission.PERSO_SNIPPET})
    public void delete(@PathVariable("id") String id) {
        snippetService.delete(id);
    }
}
