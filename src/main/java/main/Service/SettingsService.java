package main.Service;

import main.Repository.GlobalSettingsRepository;
import main.Response.SettingsReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Autowired
    GlobalSettingsRepository globalSettingsRepository;

    private SettingsReponse settingsReponse = new SettingsReponse();

    public SettingsReponse getGlobalSettings()
    {
        settingsReponse.setMultiuserMode(globalSettingsRepository.getValueByCode("MULTIUSER_MODE"));
        settingsReponse.setPostPremoderation(globalSettingsRepository.getValueByCode("POST_PREMODERATION"));
        settingsReponse.setStatisticsIsPublic(globalSettingsRepository.getValueByCode("STATISTICS_IS_PUBLIC"));
        return settingsReponse;
    }
}
