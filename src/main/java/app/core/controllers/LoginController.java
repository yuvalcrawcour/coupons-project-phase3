package app.core.controllers;

import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionUnauthorized;
import app.core.login.ClientType;
import app.core.login.LoginManager;
import app.core.token.TokensManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import app.core.services.LoginService;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private TokensManager tokensManager;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@RequestParam String email, @RequestParam String password,@RequestParam ClientType clientType) throws ResponseStatusException {
        try {
            int id = this.loginService.login(email, password,clientType);
            String token =this.tokensManager.generateToken(new TokensManager.ClientDetails(id,email,clientType));
            return token;
        } catch (CouponSystemServiceExceptionUnauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

}
}