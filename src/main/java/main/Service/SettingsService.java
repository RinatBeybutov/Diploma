package main.Service;

import java.security.Principal;
import main.Model.GlobalSettings;
import main.Model.UserModel;
import main.Repository.GlobalSettingsRepository;
import main.Repository.UserRepository;
import main.Response.SettingsReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Autowired
    GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    UserRepository userRepository;

    public SettingsReponse getGlobalSettings()
    {
        SettingsReponse settingsReponse = new SettingsReponse();
        settingsReponse.setMultiuserMode(globalSettingsRepository.getValueByCode("MULTIUSER_MODE"));
        settingsReponse.setPostPremoderation(globalSettingsRepository.getValueByCode("POST_PREMODERATION"));
        settingsReponse.setStatisticsIsPublic(globalSettingsRepository.getValueByCode("STATISTICS_IS_PUBLIC"));
        return settingsReponse;
    }

    public void setGlobalSettings(SettingsReponse settingsReponse,
        Principal principal) {

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(()-> new UsernameNotFoundException(principal.getName()));

        if(user.getIsModerator() == 1)
        {
            GlobalSettings multiuserMode = globalSettingsRepository.getGlobalSettingsByCode("MULTIUSER_MODE");
            GlobalSettings premoderation = globalSettingsRepository.getGlobalSettingsByCode("POST_PREMODERATION");
            GlobalSettings statistics = globalSettingsRepository.getGlobalSettingsByCode("STATISTICS_IS_PUBLIC");

            multiuserMode.setValue(settingsReponse.isMultiuserMode() ? "YES" : "NO");
            premoderation.setValue(settingsReponse.isPostPremoderation()? "YES" : "NO");
            statistics.setValue(settingsReponse.isStatisticsIsPublic() ? "YES" : "NO");

            globalSettingsRepository.save(multiuserMode);
            globalSettingsRepository.save(premoderation);
            globalSettingsRepository.save(statistics);
        }
    }
}
