package blog.service;

import blog.api.response.SettingsResponse;
import blog.model.GlobalSetting;
import blog.model.GlobalSettingRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;

@Service
public class SettingsService {

    private final GlobalSettingRepository globalSettingRepository;

    public SettingsService(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    public SettingsResponse getGlobalSettings()  throws EntityNotFoundException {
        boolean isMUM = false;
        boolean isPP = false;
        boolean isSIP = false;
        SettingsResponse settingsResponse = new SettingsResponse();
        HashMap<String, String> globalSettings = new HashMap<>();
        Iterable<GlobalSetting> globalSettingIterable = globalSettingRepository.findAll();
        globalSettingIterable.forEach(globalSetting -> {
            globalSettings.put(globalSetting.getCode(), globalSetting.getValue());
        });

        if(globalSettings.get("MULTIUSER_MODE").equals("YES")) isMUM = true;
        if(globalSettings.get("POST_PREMODERATION").equals("YES")) isPP = true;
        if(globalSettings.get("STATISTICS_IS_PUBLIC").equals("YES")) isSIP = true;
        settingsResponse.setMultiuserMode(isMUM);
        settingsResponse.setPostPremoderation(isPP);
        settingsResponse.setStatisticsIsPublic(isSIP);

        return settingsResponse;
    }
}
