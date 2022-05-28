package app.core;

import app.core.filters.LoginFilter;
import app.core.token.TokensManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CouponsProjectPhase3Application {

	public static void main(String[] args) {

		SpringApplication.run(CouponsProjectPhase3Application.class, args);
		}

	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter(TokensManager tokensManager) {
		FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoginFilter(tokensManager));
		registrationBean.addUrlPatterns("/api/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}

}
