package blog.controllers;

import blog.api.response.AuthCheckResponse;
import blog.service.AuthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiAuthController {

    private final AuthCheckService authCheckService;

    public ApiAuthController(AuthCheckService authCheckService) {
        this.authCheckService = authCheckService;
    }

    @GetMapping("/auth/check")
    private AuthCheckResponse getAuthCheck() {
        return authCheckService.getAuthCheck();
    }
}
