package ru.topjava.lunchvote.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.UserService;

import java.net.URI;
import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {

    static final String REST_URL = "/rest/admin/users";

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.info("Get user with id {}", id);
        return userService.get(id);
    }

    @GetMapping("/by")
    public User getByEmail(@RequestParam String email) {
        log.info("Get user with email {}", email);
        return userService.getByEmail(email);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@RequestBody User user) {
        log.info("Create user {}", user);
        checkNew(user);
        User created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created).toUri();
        return ResponseEntity.created(uriOfNewResource).body(user);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user, @PathVariable long id) {
        log.info("Update user {} with id {}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Delete user with id {}", id);
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable long id,@RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        userService.enable(id, enabled);
    }
}
