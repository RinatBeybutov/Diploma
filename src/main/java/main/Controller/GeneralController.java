package main.Controller;

import main.Response.*;
import main.Response.dto.TagDto;
import main.Service.PostService;
import main.Service.SettingsService;
import main.Service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api")
public class GeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final NotAuthorizationResponse notAuthorizationResponse;
    private PostService postService;
    private TagService tagService;

    public GeneralController(InitResponse initResponse,
                             SettingsService settingsService,
                             NotAuthorizationResponse notAuthorizationResponse,
                             PostService postService,
                             TagService tagService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.notAuthorizationResponse = notAuthorizationResponse;
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

    @GetMapping("/api/auth/check")
    public NotAuthorizationResponse getCheck()
    {
        return notAuthorizationResponse;
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

    // GET /api/statistics/all

    @GetMapping("api/statistics/all")
    public StatisticsResponse getStatistics()
    {
        return postService.getGlobalStatistics();
    }

    // POST /api/profile/my

    // PUT /api/settings/

    // POST /api/profile/my


}
