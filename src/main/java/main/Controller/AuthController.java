package main.Controller;

import main.Request.RequestLogin;
import main.Request.RequestRegister;
import main.Response.CaptchaResponse;
import main.Response.LoginResponse;
import main.Response.dto.ResultDto;
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

    // POST /api/auth/register

    @PostMapping("/register")
    public ResponseEntity<?> registerNew(@RequestBody RequestRegister requestRegister)
    {
        return userService.registerNewUser(requestRegister);
    }

    // GET /api/auth/captcha

    @GetMapping("/captcha")
    public CaptchaResponse getCaptcha() throws IOException {
        return captchaServise.getCaptcha();
    }

    // POST /api/auth/login

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin)
    {
        return userService.login(requestLogin);
    }


    // GET /api/auth/check

    // POST /api/auth/restore

    // POST /api/auth/password

    // GET /api/auth/logout




}
