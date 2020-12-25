package main.Service;

import main.Model.User;
import main.Repository.CaptchaRepository;
import main.Repository.UserRepository;
import main.Request.RequestLogin;
import main.Request.RequestRegister;
import main.Response.LoginResponse;
import main.Response.RegisterWrongResponse;
import main.Response.dto.ResultDto;
import main.Response.dto.UserBigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

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

        if(!userRepository.findAllByEmail(requestLogin.getE_mail()).isPresent())
        {
            return new ResponseEntity<>(new ResultDto(false), HttpStatus.OK);
        }
        LoginResponse loginResponse = new LoginResponse();
        User user = userRepository.findAllByEmail(requestLogin.getE_mail()).get();
        loginResponse.setUser(convertUserToUserBigDTO(user));
        loginResponse.setResult(true);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    private User convertFromRequestUserToUser(RequestRegister requestRegister) {
        User user = new User();
        user.setRegTime(new Date());
        user.setEmail(requestRegister.getE_mail());
        user.setName(requestRegister.getName());
        user.setPassword(requestRegister.getPassword());
        user.setIsModerator((byte)0);
        return user;
    }

    private UserBigDto convertUserToUserBigDTO(User user)
    {
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
}
