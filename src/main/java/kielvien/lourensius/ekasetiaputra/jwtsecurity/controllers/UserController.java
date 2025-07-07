package kielvien.lourensius.ekasetiaputra.jwtsecurity.controllers;

import java.text.ParseException;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.InvalidDataException;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.DataNotFoundExecption;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.GeneralResponses;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserSignUpRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.services.UserServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
	private UserServices userServices;

	public UserController(UserServices userServices) {
		this.userServices = userServices;
	}

//	@PreAuthorize("hasAnyRole('')")
	@PreAuthorize("hasAuthority('GET_USER')")
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponses<UserResponse> getUser(@PathVariable Long id) throws DataNotFoundExecption {
		UserResponse data = userServices.getUser(id);
		GeneralResponses<UserResponse> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.SUCCESS.getCode());
		response.setDesc(Constants.HttpStatusCode.SUCCESS.getDesc());
		response.setData(data);
		return response;
	}

	@PreAuthorize("hasAuthority('CREATE_USER')")
	@PostMapping(path = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponses<Void> signtUp(@RequestBody UserSignUpRequest signUpRequest)
			throws ParseException, InvalidDataException {
		userServices.signtUp(signUpRequest);
		GeneralResponses<Void> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.SUCCESS.getCode());
		response.setDesc(Constants.HttpStatusCode.SUCCESS.getDesc());
		return response;
	}
}
