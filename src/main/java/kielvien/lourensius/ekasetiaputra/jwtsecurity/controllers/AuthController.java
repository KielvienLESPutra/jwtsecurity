package kielvien.lourensius.ekasetiaputra.jwtsecurity.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.FailLoginException;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.GeneralResponses;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.services.AuthService;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponses<AuthLoginResponse> login(@RequestBody AuthLoginRequest authLoginRequest)
			throws FailLoginException {
		String token = authService.login(authLoginRequest);
		AuthLoginResponse authLoginResponse = new AuthLoginResponse();
		authLoginResponse.setToken(token);

		GeneralResponses<AuthLoginResponse> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.SUCCESS.getCode());
		response.setDesc(Constants.HttpStatusCode.SUCCESS.getDesc());
		response.setData(authLoginResponse);

		return response;
	}
}
