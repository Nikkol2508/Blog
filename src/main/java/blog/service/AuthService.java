package blog.service;

import blog.api.response.AuthCheckResponse;
import blog.api.response.CaptchaResponse;
import blog.model.CaptchaCode;
import blog.repositorys.CaptchaRepository;
import com.github.cage.Cage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    private final CaptchaRepository captchaRepository;

    @Value("${blog.captchaCodeStorageTime}")
    int captchaCodeStorageTime;

    public AuthService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    public AuthCheckResponse getAuthCheck() {
        AuthCheckResponse authCheck = new AuthCheckResponse();
        authCheck.setResult(false);
        return authCheck;
    }

    public CaptchaResponse getCaptcha() {

        captchaRepository.deleteOld(captchaCodeStorageTime);

        CaptchaResponse captchaResponse = new CaptchaResponse();
        Cage cage = new Cage();

        String code = cage.getTokenGenerator().next();
        String secret = RandomStringUtils.randomAlphanumeric(22);

        byte[] fileContent = cage.draw(code);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        captchaResponse.setSecret(secret);
        captchaResponse.setImage("data:image/png;base64, " + encodedString);

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secret);
        captchaCode.setTime(System.currentTimeMillis() / 1000);

        try {
            captchaRepository.save(captchaCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return captchaResponse;
    }

}
