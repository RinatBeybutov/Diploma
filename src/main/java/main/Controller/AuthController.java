package main.Controller;

import java.security.Principal;
import main.Request.RequestLogin;
import main.Request.RequestPassword;
import main.Request.RequestRegister;
import main.Request.RequestRestore;
import main.Response.CaptchaResponse;
import main.Service.CaptchaServise;
import main.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    CaptchaServise captchaServise;
    UserService userService;

    public AuthController(CaptchaServise captchaServise, UserService userService) {
        this.captchaServise = captchaServise;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNew(@RequestBody RequestRegister requestRegister)
    {
        return userService.registerNewUser(requestRegister);
    }

    @GetMapping("/captcha")
    public CaptchaResponse getCaptcha() throws IOException {
        return captchaServise.getCaptcha();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin)
    {
        return userService.login(requestLogin);
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(Principal principal)
    {
        return userService.check(principal);
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restorePassword(@RequestBody RequestRestore requestRestore)
    {
        return userService.restorePassword(requestRestore);
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody RequestPassword requestPassword)
    {
        return userService.changePassword(requestPassword);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(Principal principal)
    {
        return userService.logout(principal);
    }

}
