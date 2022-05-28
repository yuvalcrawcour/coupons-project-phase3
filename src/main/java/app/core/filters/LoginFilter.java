package app.core.filters;

import app.core.token.TokensManager;
import app.core.token.TokensManager.ClientDetails;
import app.core.token.TokensManager;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

	private TokensManager tokensManager;

	public LoginFilter(TokensManager tokensManager) {
		super();
		this.tokensManager = tokensManager;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String token = req.getHeader("token");
		System.out.println("===== LOGIN FILTER TOKEN: " + token);

		if (token == null && req.getMethod().equals("OPTIONS")) {
			System.out.println("this is preflight request: " + req.getMethod());
			chain.doFilter(request, response);
			return;
		}

		try {

			TokensManager.ClientDetails clientDetails = tokensManager.extractClient(token);
			System.out.println("===== LOGIN FILTER: " + clientDetails);
			chain.doFilter(request, response);

			return;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			resp.sendError(HttpStatus.UNAUTHORIZED.value(), "not logged in");
		}

	}

}
