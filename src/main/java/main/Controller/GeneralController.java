package main.Controller;

import java.security.Principal;
import main.Request.RequestComment;
import main.Request.RequestDecisionModeration;
import main.Response.*;
import main.Service.PostService;
import main.Service.SettingsService;
import main.Service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api")
public class GeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private PostService postService;
    private TagService tagService;

    public GeneralController(InitResponse initResponse,
                             SettingsService settingsService,
                             PostService postService,
                             TagService tagService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/api/init")
    public InitResponse getInfo()
    {
        return initResponse;
    }

    @GetMapping("/api/settings")
    public SettingsReponse getSettings()
    {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/api/tag")
    public TagsResponse getTags()
    {
        return tagService.getTags();
    }

    @GetMapping("/api/calendar")
    public CalendarResponse getCalendar(@RequestParam int year)
    {
       return postService.getCalendarResponse(year);
    }

    @GetMapping("api/statistics/all")
    public ResponseEntity<StatisticsResponse>  getStatistics()
    {
        return postService.getGlobalStatistics();
    }

    @GetMapping("api/statistics/my")
    public ResponseEntity<StatisticsResponse> getMyStatistics(Principal principal)
    {
        return postService.getMyStatistics(principal);
    }

    @PutMapping("api/settings")
    public void setSettings(@RequestBody SettingsReponse settingsReponse, Principal principal)
    {
        settingsService.setGlobalSettings(settingsReponse, principal);
    }

    @PostMapping("api/moderation")
    public ResponseEntity<?> postModeration(Principal principal,
        @RequestBody RequestDecisionModeration requestDecisionModeration)
    {
        return postService.moderateNewPosts(principal, requestDecisionModeration);
    }

    @PostMapping("api/comment")
    public ResponseEntity<?> comment(@RequestBody RequestComment requestComment, Principal principal)
    {
        return postService.comment(requestComment, principal);
    }

    // POST /api/profile/my

    @PostMapping("api/profile/my")
    public ResponseEntity<?> editProfile()
    {
        
    }

}
