package ru.topjava.lunchvote.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.UserService;
import ru.topjava.lunchvote.to.UserTo;
import ru.topjava.lunchvote.web.security.AuthorizedUser;
import ru.topjava.lunchvote.web.security.SecurityUtil;

import java.net.URI;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileUserController {

    static final String REST_URL = "/rest/profile";

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ProfileUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Get user {}", authorizedUser.getUsername());
        return userService.get(authorizedUser.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Delete user {}", authorizedUser.getUsername());
        userService.delete(authorizedUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Update user {} with id {}", userTo, authorizedUser.getId());
        assureIdConsistent(userTo, authorizedUser.getId());
        userService.update(userTo);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody UserTo userTo) {
        log.info("Create user {}", userTo);
        checkNew(userTo);
        User created = userService.create(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
