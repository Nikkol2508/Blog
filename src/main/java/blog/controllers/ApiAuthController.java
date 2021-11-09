package blog.controllers;

import blog.api.response.AuthCheckResponse;
import blog.api.response.CaptchaResponse;
import blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authCheckService) {
        this.authService = authCheckService;
    }

    @GetMapping("/check")
    private AuthCheckResponse getAuthCheck() {
        return authService.getAuthCheck();
    }

    @GetMapping("/captcha")
    private ResponseEntity<CaptchaResponse> getCaptcha() {
        return new ResponseEntity<>(authService.getCaptcha(), HttpStatus.OK);
    }
}
