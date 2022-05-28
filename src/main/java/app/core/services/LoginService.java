package app.core.services;

import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CouponSystemServiceException;
import app.core.exceptions.CouponSystemServiceExceptionUnauthorized;
import app.core.login.ClientType;
import app.core.repositories.CompanyRepository;
import app.core.token.TokensManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LoginService {

    @Value("${app.login.email}")
    private String email;
    @Value("${app.login.password}")
    private String password;
    @Autowired
    private TokensManager tokensManager;
    @Autowired
    private CompanyRepository companyRepository;

    public String login(String email, String password, ClientType clientType) throws CouponSystemServiceExceptionUnauthorized {
        if (clientType == ClientType.ADMIN && email.equals(this.email) && password.equals(this.password))
            return tokensManager.generateAdminToken(this.email);

        if (clientType == ClientType.COMPANY && companyRepository.existsByEmailAndPassword(email,password)) {
            return tokensManager.generateToken(new TokensManager.ClientDetails(companyRepository.getByEmailAndPassword(email,password).getId(), "email", clientType));
        }
        throw new CouponSystemServiceExceptionUnauthorized("login faild - bad credentials");


    }
}
