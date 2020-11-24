package main.Controller;

import main.response.responseInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    @GetMapping("/api/init/")
    public responseInfo getInfo()
    {
        return new responseInfo("DevPub", "Рассказы разработчиков","+7 903 666-44-55", "mail@mail.ru", "Дмитрий Сергеев","2005");
    }

    // GET /api/settings/

    // PUT /api/settings/



}
