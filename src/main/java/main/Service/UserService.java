package main.Service;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import main.Model.UserModel;
import main.Repository.CaptchaRepository;
import main.Repository.UserRepository;
import main.Request.RequestLogin;
import main.Request.RequestPassword;
import main.Request.RequestRegister;
import main.Request.RequestRestore;
import main.Response.EditProfileWrongResponse;
import main.Response.ImageWrongResponse;
import main.Response.LoginResponse;
import main.Response.PasswordWrongResponse;
import main.Response.RegisterWrongResponse;
import main.Dto.ResultDto;
import main.Dto.UserBigDto;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaMailSender mailSender;

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
            userRepository.save(convertFromRequestUserToUser(requestRegister));
            return new ResponseEntity<>(new ResultDto(true), HttpStatus.OK);
        }

        return new ResponseEntity<>(registerWrongResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> login(RequestLogin requestLogin) {

        if (!userRepository.findAllByEmail(requestLogin.getEmail()).isPresent()) {
            return new ResponseEntity<>(new ResultDto(false), HttpStatus.OK);
        }

        Authentication auth = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(),
                requestLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User userDetails = (User) auth.getPrincipal();

        return new ResponseEntity<>(getLoginResponse(requestLogin.getEmail()), HttpStatus.OK);
    }

    private UserModel convertFromRequestUserToUser(RequestRegister requestRegister) {
        UserModel user = new UserModel();
        user.setRegTime(new Date());
        user.setEmail(requestRegister.getE_mail());
        user.setName(requestRegister.getName());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(requestRegister.getPassword()));
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

        if (!userRepository.findAllByEmail(requestRestore.getEmail()).isPresent()) {
            return ResponseEntity.ok(new ResultDto(false));
        }
        UserModel user = userRepository.findAllByEmail(requestRestore.getEmail()).get();
        SimpleMailMessage message = new SimpleMailMessage();
        String hash = "" +
            message.hashCode() +
            Math.abs(Double.toString(Math.random() * 100000).hashCode()) +
            Math.abs(Double.toString(Math.random() * 100000).hashCode());

        user.setCode(hash);
        userRepository.save(user);

        message.setFrom("rinatbeibutov@gmail.com");
        message.setSubject("Restore password");
        message.setText("http://localhost:8080/login/change-password/" + hash);
        message.setTo(requestRestore.getEmail());

        mailSender.send(message);

        return ResponseEntity.ok(new ResultDto(true));
    }

    public ResponseEntity<?> changePassword(RequestPassword requestPassword) {

        boolean isCorrectCaptcha = true;
        boolean isLongPassword = true;
        boolean isActiveCode = true;

        PasswordWrongResponse passwordWrongResponse = new PasswordWrongResponse();

        if (requestPassword.getPassword().length() < 6) {
            isLongPassword = false;
            passwordWrongResponse.getErrors().setPassword("Пароль короче 6-ти символов");
        }

        if (!captchaRepository.findAllBySecret_code(requestPassword.getCaptchaSecret())
            .getCode().equals(requestPassword.getCaptcha())) {
            isCorrectCaptcha = false;
            passwordWrongResponse.getErrors().setCaptcha("Код с картинки введён неверно");
        }

        if (!userRepository.findAllByCode(requestPassword.getCode()).isPresent()) {
            isActiveCode = false;
            passwordWrongResponse.getErrors().setCode("Ссылка для восстановления пароля устарела.\n"
                + "\t\t\t\t<a href=\n"
                + "\t\t\t\t\\\"/auth/restore\\\">Запросить ссылку снова</a>\",");
        }

        if (isActiveCode && isCorrectCaptcha && isLongPassword) {
            UserModel user = userRepository.findAllByCode(requestPassword.getCode()).get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(requestPassword.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>(new ResultDto(true), HttpStatus.OK);
        }

        return new ResponseEntity<>(passwordWrongResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> editProfile(String requestBody, MultipartFile photo, String emailMP,
        String nameMP, String passwordMP, String removePhotoMP, Principal principal)
        throws  org.json.simple.parser.ParseException {

        String email = emailMP;
        String name = nameMP;
        String password = passwordMP;
        String removePhoto = removePhotoMP;

        if (requestBody != null) {

            JSONObject request = (JSONObject) new org.json.simple.parser.JSONParser().parse(requestBody);

            if (request.containsKey("email")) {
                email = request.get("email").toString();
            }
            if (request.containsKey("name")) {
                name = request.get("name").toString();
            }
            if (request.containsKey("password")) {
                password = request.get("password").toString();
            }
            if (request.containsKey("removePhoto")) {
                removePhoto = request.get("removePhoto").toString();
            }
        }

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        EditProfileWrongResponse editProfileWrongResponse = new EditProfileWrongResponse();

        boolean isLongPassword = true;
        boolean isNewEmail = true;
        boolean isSmallPhoto = true;
        boolean isCorrectName = true;

        if (!user.getEmail().equals(email) && email != null) {

            if (!userRepository.findAllByEmail(email).isPresent()) {
                user.setEmail(email);
            } else {
                editProfileWrongResponse.getErrors().setEmail("Этот e-mail уже зарегистрирован");
                isNewEmail = false;
            }
        }

        if (!user.getName().equals(name) && name != null) {
            user.setName(name);
        }

        if (password != null) {
            if (password.length() >= 6) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
                user.setPassword(encoder.encode(password));
            } else {
                editProfileWrongResponse.getErrors().setPassword("Пароль короче 6-ти символов");
                isLongPassword = false;
            }
        }

        if (removePhoto != null && removePhoto.equals("1") && user.getPhoto() != null) {
            deletePhoto(user.getPhoto());
            user.setPhoto(null);

        } else if(photo != null) {
            if (photo.getSize() < 5 * 1024 * 1024) {
                this.setPhotoToUser(photo, principal);
            } else {
                editProfileWrongResponse.getErrors()
                    .setPhoto("Фото слишком большое, нужно не более 5 Мб");
            }
        }

        if (isCorrectName && isLongPassword && isNewEmail && isSmallPhoto) {
            userRepository.save(user);
            return ResponseEntity.ok(new ResultDto(true));
        } else {
            return ResponseEntity.ok(editProfileWrongResponse);
        }
    }

    private void deletePhoto(String pathPhoto) {

        File deleteFile = new File(pathPhoto.substring(1));
        deleteFile.delete();
    }

    public ResponseEntity<?> saveImage(MultipartFile image) {
        ImageWrongResponse imageWrongResponse = new ImageWrongResponse();
        if (image.getSize() > 5 * 1024 * 1024) {
            imageWrongResponse.getErrors().setImage("Размер файла превышает допустимый размер");
            return ResponseEntity.ok(imageWrongResponse);
        }

        createUploadFolder();

        File fileImage = new File("${upload.folder}" + image.getOriginalFilename());

        try {
            image.transferTo(fileImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("\\/" + fileImage.getPath());
    }

    public ResponseEntity<?> setPhotoToUser(MultipartFile photo, Principal principal) {

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        createUploadFolder();

        File filePhoto = new File("${upload.folder}" + photo.getOriginalFilename());

        try {
            photo.transferTo(filePhoto);
            user.setPhoto("/" + filePhoto.getPath());
            userRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("\\/" + filePhoto.getPath());
    }

    private void createUploadFolder() {
        File folder = new File("/${upload.folder}");

        if(!folder.exists()) {
            folder.mkdir();
        }
    }

    public ResponseEntity<?> logout(Principal principal) {

        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseEntity.ok(new ResultDto(true));
    }
}
