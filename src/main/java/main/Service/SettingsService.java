package main.Service;

import main.Repository.GlobalSettingsRepository;
import main.Response.SettingsReponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private SettingsReponse settingsReponse = new SettingsReponse();

    public SettingsReponse getGlobalSettings()
    {
        String str;
        settingsReponse.setMultiuserMode(false);
        settingsReponse.setPostPremoderation(true);
        settingsReponse.setStatisticsIsPublic(true);
        return settingsReponse;
    }

}
