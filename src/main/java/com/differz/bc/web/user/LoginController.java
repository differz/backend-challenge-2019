package com.differz.bc.web.user;

import com.differz.bc.core.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private static final String USER_LOGIN = "user login";

    private final UserService userService;

    @PostMapping("/login")
    public LoginDto login(@RequestBody LoginInputDto loginInputDto) {
        Optional<User> existedUser = userService.findUserIfExists(loginInputDto);
        User user = existedUser.orElseGet(() -> userService.registerUser(loginInputDto));
        log.debug(USER_LOGIN);
        return LoginDto.of(user);
    }
}
