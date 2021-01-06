package main.Service;

import java.security.Principal;
import java.util.Date;
import main.Model.UserModel;
import main.Repository.CaptchaRepository;
import main.Repository.UserRepository;
import main.Request.RequestLogin;
import main.Request.RequestRegister;
import main.Request.RequestRestore;
import main.Response.LoginResponse;
import main.Response.RegisterWrongResponse;
import main.dto.ResultDto;
import main.dto.UserBigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> registerNewUser(RequestRegister requestRegister) {

        boolean isUniqueEmail = true;
        boolean isLongPassword = true;
        boolean isCorrectName = true;
        boolean isCorrectCaptcha = true;

        RegisterWrongResponse registerWrongResponse = new RegisterWrongResponse();

        if(requestRegister.getPassword().length() < 6)
        {
            isLongPassword = false;
            registerWrongResponse.getErrors().setPassword("Пароль короче 6-ти символов");
        }

        if (userRepository.findAllByEmail(requestRegister.getE_mail()).isPresent())
        {
            isUniqueEmail = false;
            registerWrongResponse.getErrors().setEmail("Этот e-mail уже зарегистрирован");
        }

        if(!captchaRepository.findAllBySecret_code(requestRegister.getCaptcha_secret())
                .getCode().equals(requestRegister.getCaptcha()))
        {
            isCorrectCaptcha = false;
            registerWrongResponse.getErrors().setCaptcha("Код с картинки введён неверно");
        }

        if(isCorrectCaptcha && isCorrectName && isLongPassword && isUniqueEmail)
        {
            ResultDto resultDto = new ResultDto(true);
            userRepository.save(convertFromRequestUserToUser(requestRegister));
            return new ResponseEntity<>(resultDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(registerWrongResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> login(RequestLogin requestLogin) {

        Authentication auth = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getE_mail(),
                requestLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User userDetails = (User) auth.getPrincipal();

        if (!userRepository.findAllByEmail(requestLogin.getE_mail()).isPresent()) {
            return new ResponseEntity<>(new ResultDto(false), HttpStatus.OK);
        }

        return new ResponseEntity<>(getLoginResponse(requestLogin.getE_mail()), HttpStatus.OK);
    }

    private UserModel convertFromRequestUserToUser(RequestRegister requestRegister) {
        UserModel user = new UserModel();
        user.setRegTime(new Date());
        user.setEmail(requestRegister.getE_mail());
        user.setName(requestRegister.getName());
        user.setPassword(requestRegister.getPassword());
        user.setIsModerator((byte) 0);
        return user;
    }

    private UserBigDto convertUserToUserBigDTO(UserModel user) {
        UserBigDto userBigDto = new UserBigDto();
        userBigDto.setId(user.getId());
        userBigDto.setName(user.getName());
        userBigDto.setEmail(user.getEmail());
        userBigDto.setModeration(user.getIsModerator() == 1);
        userBigDto.setModerationCount(0);
        userBigDto.setSettings(true);
        userBigDto.setPhoto(user.getPhoto() == null ? "" : user.getPhoto());
        return userBigDto;
    }

    public ResponseEntity<?> check(Principal principal) {
        if(principal == null)
        {
            return ResponseEntity.ok(new ResultDto(false));
        }
        return ResponseEntity.ok(getLoginResponse(principal.getName()));
    }

    private LoginResponse getLoginResponse(String email)
    {
        UserModel user = userRepository.findAllByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(convertUserToUserBigDTO(user));
        loginResponse.setResult(true);
        return loginResponse;
    }

    public ResponseEntity<?> restorePassword(RequestRestore requestRestore) {

        return null;
    }
}
