package blog.service;

import blog.api.response.SettingsResponse;
import blog.model.GlobalSetting;
import blog.model.GlobalSettingRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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
        GlobalSetting mUM = globalSettingRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Setting not found by id: " + 1));
        GlobalSetting pP = globalSettingRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("Setting not found by id: " + 2));
        GlobalSetting sIP = globalSettingRepository.findById(3).orElseThrow(() -> new EntityNotFoundException("Setting not found by id: " + 3));
        if(mUM.getValue().equals("YES")) isMUM = true;
        if(pP.getValue().equals("YES")) isPP = true;
        if(sIP.getValue().equals("YES")) isSIP = true;
        settingsResponse.setMultiuserMode(isMUM);
        settingsResponse.setPostPremoderation(isPP);
        settingsResponse.setStatisticsIsPublic(isSIP);

        return settingsResponse;
    }
}
