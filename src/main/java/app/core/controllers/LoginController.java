package app.core.controllers;

import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CouponSystemServiceException;
import app.core.login.ClientType;
import app.core.login.LoginManager;
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

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@RequestParam String email, @RequestParam String password,@RequestParam ClientType clientType) throws ResponseStatusException {
        try {
            String token = this.loginService.login(email, password,clientType);
            return token;
        } catch (CouponSystemException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

}
}