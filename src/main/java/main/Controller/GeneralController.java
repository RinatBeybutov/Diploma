package main.Controller;

import main.Response.*;
import main.Service.PostService;
import main.Service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api")
public class GeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final NotAuthorizationResponse notAuthorizationResponse;
    private PostService postService;

    public GeneralController(InitResponse initResponse,
                             SettingsService settingsService,
                             NotAuthorizationResponse notAuthorizationResponse,
                             PostService postService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.notAuthorizationResponse = notAuthorizationResponse;
        this.postService = postService;
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

    @GetMapping("/api/auth/check")
    public NotAuthorizationResponse getCheck()
    {
        return notAuthorizationResponse;
    }

    @GetMapping("/api/tag")
    public List<TagsResponse> getTags()
    {
        return new ArrayList<TagsResponse>();
    }

    // GET /api/calendar/

    @GetMapping("/api/calendar/{id}")
    public CalendarResponse getCalendar(@PathVariable int id)
    {
        System.out.println("id = " + id);
       return postService.getCalendarResponse(id);
    }

    // POST /api/profile/my

    // PUT /api/settings/

    // POST /api/profile/my


}
