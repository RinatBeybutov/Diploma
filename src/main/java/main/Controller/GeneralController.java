package main.Controller;

import main.Response.InitResponse;
import main.Response.NotAuthorizationResponse;
import main.Response.SettingsReponse;
import main.Service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // GET /api/settings/

    // PUT /api/settings/



}
