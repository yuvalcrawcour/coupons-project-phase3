package app.core.login;


import app.core.exceptions.CouponSystemServiceException;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoginManager {
    private static LoginManager instance;
    ConfigurableApplicationContext ctx;

    @Autowired
    private LoginManager (ConfigurableApplicationContext ctx){
        this.ctx = ctx;
    }

    public static LoginManager getInstance(ConfigurableApplicationContext ctx) {
        instance = new LoginManager(ctx);
        return instance;
    }

    public ClientService login (String email, String password , ClientType clientType) throws CouponSystemServiceException {
        ClientService clientService = null;
        try{
            switch (clientType){
                case ADMIN:
                    clientService = ctx.getBean(AdminService.class);
                    break;
                case COMPANY:
                    clientService = ctx.getBean(CompanyService.class);
                    break;
                case CUSTOMER:
                    clientService = ctx.getBean(CustomerService.class);
                    break;
                default:
                    throw new IllegalArgumentException("unexpected value");
            }
            if(clientService.login(email, password)){
                return clientService;
            }
            else{
                return null;
            }

        } catch (CouponSystemServiceException e) {
            throw new CouponSystemServiceException(e);
        }

    }
}
