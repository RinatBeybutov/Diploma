package main.Controller;

import java.security.Principal;
import main.Request.RequestComment;
import main.Request.RequestDecisionModeration;
import main.Request.RequestEditProfile;
import main.Response.CalendarResponse;
import main.Response.InitResponse;
import main.Response.SettingsReponse;
import main.Response.StatisticsResponse;
import main.Response.TagsResponse;
import main.Service.PostService;
import main.Service.SettingsService;
import main.Service.TagService;
import main.Service.UserService;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class GeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private PostService postService;
    private TagService tagService;
    private UserService userService;

    public GeneralController(InitResponse initResponse,
        SettingsService settingsService,
        PostService postService,
        TagService tagService,
        UserService userService) {

        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/init")
    public InitResponse getInfo()
    {
        return initResponse;
    }

    @GetMapping("/settings")
    public SettingsReponse getSettings()
    {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    public TagsResponse getTags()
    {
        return tagService.getTags();
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(@RequestParam int year)
    {
       return postService.getCalendarResponse(year);
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponse>  getStatistics()
    {
        return postService.getGlobalStatistics();
    }

    @GetMapping("/statistics/my")
    public ResponseEntity<StatisticsResponse> getMyStatistics(Principal principal)
    {
        return postService.getMyStatistics(principal);
    }

    @PutMapping("/settings")
    public void setSettings(@RequestBody SettingsReponse settingsReponse, Principal principal) {
        settingsService.setGlobalSettings(settingsReponse, principal);
    }

    @PostMapping("/moderation")
    public ResponseEntity<?> postModeration(Principal principal,
        @RequestBody RequestDecisionModeration requestDecisionModeration) {
        return postService.moderateNewPosts(principal, requestDecisionModeration);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody RequestComment requestComment,
        Principal principal) {
        return postService.comment(requestComment, principal);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile photo, Principal principal)
    {
        return userService.savePhoto(photo, principal);
    }

    @PostMapping(value = "/profile/my", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<?> postApiProfileMy(@RequestBody(required = false) String requestBody,
        @RequestPart(value = "photo", required = false) MultipartFile photo,
        @RequestPart(value = "email", required = false) String emailMP,
        @RequestPart(value = "name", required = false) String nameMP,
        @RequestPart(value = "password", required = false) String passwordMP,
        @RequestPart(value = "removePhoto", required = false) String removePhotoMP,
        Principal principal)
        throws org.json.simple.parser.ParseException {
        return userService
            .editProfile(requestBody, photo, emailMP, nameMP, passwordMP, removePhotoMP, principal);
    }


}
