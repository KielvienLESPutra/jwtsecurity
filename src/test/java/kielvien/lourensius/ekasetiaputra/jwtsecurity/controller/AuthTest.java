package kielvien.lourensius.ekasetiaputra.jwtsecurity.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.data.DataPreparation;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.User;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.GeneralResponses;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.CustomerRepository;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AuthTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private CustomerRepository customerRepository;

	private DataPreparation dataPreparation;

	@BeforeEach
	void setUp() throws Exception {
		dataPreparation = new DataPreparation();
		when(userRepository.findById(1L)).thenReturn(Optional.of(dataPreparation.getUser()));
		when(userRepository.findByUsername("Kielvien")).thenReturn(Optional.of(dataPreparation.getUser()));
		when(userRepository.findByEmail("for2university@gmail.com")).thenReturn(Optional.of(dataPreparation.getUser()));
		when(userRepository.findUserWihRolesAndPermissions(1L))
				.thenReturn(Optional.of(dataPreparation.getListUserRolePermissions()));
	}

	@Test
	void loginSuccessWithUsername() throws Exception {
		AuthLoginRequest request = new AuthLoginRequest();
		request.setUsername("Kielvien");
		request.setPassword("Password09");

		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andDo(resultHandler -> {
					GeneralResponses<AuthLoginResponse> response = mapper
							.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
							});

					assertNotNull(response);
					assertNotNull(response.getDesc());

					assertEquals(Constants.HttpStatusCode.SUCCESS.getCode(), response.getStatusCode());
				});
	}

	@Test
	void loginSuccessWithEmail() throws Exception {
		AuthLoginRequest request = new AuthLoginRequest();
		request.setUsername("for2university@gmail.com");
		request.setPassword("Password09");

		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andDo(resultHandler -> {
					GeneralResponses<AuthLoginResponse> response = mapper
							.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
							});

					assertNotNull(response);
					assertNotNull(response.getDesc());

					assertEquals(Constants.HttpStatusCode.SUCCESS.getCode(), response.getStatusCode());
				});
	}

	@Test
	void loginFailInactive() throws Exception {
		AuthLoginRequest request = new AuthLoginRequest();
		request.setUsername("Kielvien");
		request.setPassword("Password09");

		dataPreparation.getUser().setActive(false);

		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void loginFailsInvalidData() throws Exception {
		AuthLoginRequest request = new AuthLoginRequest();
		request.setUsername("Kielvien");
		request.setPassword("Password39");
		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());
		
		request.setUsername("Kielvien123");
		request.setPassword("Password09");
		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());
		
		request.setUsername("Kielvien@gmail.com");
		request.setPassword("Password09");
		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());
		
	}

	@Test
	void loginFailMaxAttempt() throws Exception {
		AuthLoginRequest request = new AuthLoginRequest();
		request.setUsername("Kielvien");
		request.setPassword("Password39");

		dataPreparation.getUser().setFailLoginCount(3);

		mockMvc.perform(
				post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", "id").content(mapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());

		boolean isActiveUser = userRepository.findById(1L).map(User::isActive).orElse(true);
		assertFalse(isActiveUser);
	}
}
