package main.Controller;

import main.Model.Post;
import main.Response.*;
import main.Service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final NotAuthorizationResponse notAuthorizationResponse;

    public GeneralController(InitResponse initResponse, SettingsService settingsService, NotAuthorizationResponse notAuthorizationResponse) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.notAuthorizationResponse = notAuthorizationResponse;
    }

    @GetMapping("/api/init")
    public InitResponse getInfo()
    {
        return initResponse;
      /*  return new InitResponse("DevPub",
                "Рассказы разработчиков",
                "+7 903 666-44-55",
                "mail@mail.ru",
                "Дмитрий Сергеев",
                "2005"); */
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

    @GetMapping("api/post")
    public ListPostsResponse getPosts()
    {
        ListPostsResponse listPostsResponse = new ListPostsResponse();
        listPostsResponse.setCount(0);
        listPostsResponse.setPosts(new ArrayList<Post>());
        return listPostsResponse;
    }


    // PUT /api/settings/



}
