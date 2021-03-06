package org.crunchytorch.coddy.user.api;

import org.crunchytorch.coddy.application.data.MediaType;
import org.crunchytorch.coddy.application.data.Page;
import org.crunchytorch.coddy.application.exception.EntityExistsException;
import org.crunchytorch.coddy.application.exception.EntityNotFoundException;
import org.crunchytorch.coddy.security.data.JWTToken;
import org.crunchytorch.coddy.security.data.Permission;
import org.crunchytorch.coddy.user.data.in.Credential;
import org.crunchytorch.coddy.user.data.in.UpdateUser;
import org.crunchytorch.coddy.user.data.out.SimpleUser;
import org.crunchytorch.coddy.user.elasticsearch.entity.UserEntity;
import org.crunchytorch.coddy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the endpoint api which contains all method in order to manage the {@link org.crunchytorch.coddy.user.elasticsearch.entity.UserEntity users}
 * All the business code is in the service part, especially in the class {@link UserService}.
 */
@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON)
@CrossOrigin
public class User {

    @Autowired
    private UserService service;

    /**
     * return a token in json format if the user has entered correct credentials
     *
     * @param credential : user login and password in json format
     *                   <p>
     *                   {@code
     *                   {
     *                   "login": "jdoh",
     *                   "password": "U54dcS"
     *                   }
     *                   }
     *                   </p>
     */
    @PostMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON)
    public JWTToken authenticate(@RequestBody final Credential credential) {
        return this.service.authenticate(credential);
    }

    /**
     * This method will create a {@link org.crunchytorch.coddy.user.elasticsearch.entity.UserEntity user} from the {@link Credential credentials}.
     *
     * @param user the personnal information needed to create the associated user
     * @return the {@link SimpleUser user} created
     * @throws EntityExistsException if the given user already exists
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON)
    public SimpleUser create(@RequestBody UpdateUser user) {
        return this.service.create(user);
    }

    @PutMapping(path = "{login}", consumes = MediaType.APPLICATION_JSON)
    @PreAuthorize("hasRole('" + Permission.ADMIN + "') or (hasRole('" + Permission.USER + "') and @userSecurityService.ownsAccount(#login))")
    public SimpleUser update(@PathVariable final String login, final UpdateUser user) {
        user.setLogin(login);
        return this.service.update(user);
    }

    /**
     * This method will delete the {@link org.crunchytorch.coddy.user.elasticsearch.entity.UserEntity user} from the given login.
     *
     * @param login the user's login
     * @throws EntityNotFoundException if the given login is not associated to a {@link SimpleUser user}
     */
    @DeleteMapping(path = "{login}")
    @PreAuthorize("hasRole('" + Permission.ADMIN + "') or (hasRole('" + Permission.USER + "') and @userSecurityService.ownsAccount(#login))")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final String login) {
        this.service.delete(login);
    }

    @GetMapping()
    @PreAuthorize("hasRole('" + Permission.ADMIN + "')")
    public Page<SimpleUser> getUsers(@RequestParam(value = "from", defaultValue = "0") final int from,
                                     @RequestParam(value = "size", defaultValue = "10") final int size) {
        Page<UserEntity> oldPage = this.service.getEntity(from, size);
        return new Page<>(oldPage.getTotalElement(),
                oldPage.getTotalPage(), oldPage.getHits().stream().map(SimpleUser::new).collect(Collectors.toList()));
    }

    @GetMapping(path = "/search")
    @PreAuthorize("hasRole('" + Permission.ADMIN + "')")
    public List<SimpleUser> search(@RequestParam(value = "login") final String loginToSearch,
                                   @RequestParam(value = "from", defaultValue = "0") final int from,
                                   @RequestParam(value = "size", defaultValue = "10") final int size) {
        return this.service.search(loginToSearch, from, size);
    }

    /**
     * @param login the user's login
     * @return the {@link SimpleUser user} associated to the given login. All critical information such as his password
     * or the salt has been deleted previously
     */
    @GetMapping(path = "{login}")
    @PreAuthorize("hasRole('" + Permission.ADMIN + "') or (hasRole('" + Permission.USER + "') and @userSecurityService.ownsAccount(#login))")
    public SimpleUser getUserByLogin(@PathVariable final String login) {
        return this.service.getUserByLogin(login);
    }
}
