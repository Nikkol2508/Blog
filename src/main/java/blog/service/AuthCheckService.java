package blog.service;

import blog.api.response.AuthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthCheckService {

    public AuthCheckResponse getAuthCheck() {
        AuthCheckResponse authCheck = new AuthCheckResponse();
        authCheck.setResult(false);
        return authCheck;
    }
}
