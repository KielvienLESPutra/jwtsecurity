package kielvien.lourensius.ekasetiaputra.jwtsecurity.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.data.DataPreparation;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.Customer;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.User;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.GeneralResponses;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserSignUpRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.CustomerRepository;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class UserTests {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MessageSource messageSource;

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private CustomerRepository customerRepository;

	private String token;

	@BeforeEach
	void setUp() throws Exception {
		DataPreparation dataPreparation = new DataPreparation();

		when(userRepository.findById(1L)).thenReturn(Optional.of(dataPreparation.getUser()));
		when(userRepository.findByUsername("Kielvien")).thenReturn(Optional.of(dataPreparation.getUser()));
		when(userRepository.findByEmail("for2university@gmail.com")).thenReturn(Optional.of(dataPreparation.getUser()));
		when(userRepository.findUserWihRolesAndPermissions(1L))
				.thenReturn(Optional.of(dataPreparation.getListUserRolePermissions()));

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
					assertNotNull(response.getData());

					assertEquals(Constants.HttpStatusCode.SUCCESS.getCode(), response.getStatusCode());
					token = response.getData().getToken();
				});
	}

	@Nested
	class getUser {
		@Test
		void getUserSuccessDefault() throws Exception {
			mockMvc.perform(get("/api/user/1").accept(MediaType.APPLICATION_JSON).header("Accept-Language", "id")
					.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNotNull(response.getData());

						assertEquals(Constants.HttpStatusCode.SUCCESS.getCode(), response.getStatusCode());
						assertEquals(Constants.HttpStatusCode.SUCCESS.getDesc(), response.getDesc());
						assertEquals("Kielvien", response.getData().getUsername());
						assertEquals("Lourensius", response.getData().getLastName());
						assertNotNull(response.getData().getLastLogin());
						assertTrue(response.getData().isActive());
						assertEquals("30-11-1994", response.getData().getBod());
						assertEquals("Jakarta", response.getData().getAddress());
					});
		}

		@Test
		void getUserFailDataNotFoundEn() throws Exception {
			mockMvc.perform(
					get("/api/user/2").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
							.header("Accept-Language", "En").header("Authorization", "Bearer " + token))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.DATA_NOT_FOUND.getCode(), response.getStatusCode());
						Locale locale = new Locale.Builder().setLanguage("en").setRegion("EN").build();
						assertEquals(messageSource.getMessage("exception.dataNotFound", null, locale),
								response.getDesc());
					});
		}

		@Test
		void getUserFailDataNotFoundId() throws Exception {
			mockMvc.perform(
					get("/api/user/2").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
							.header("Accept-Language", "id").header("Authorization", "Bearer " + token))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.DATA_NOT_FOUND.getCode(), response.getStatusCode());
						Locale locale = new Locale.Builder().setLanguage("id").setRegion("ID").build();
						assertEquals(messageSource.getMessage("exception.dataNotFound", null, locale),
								response.getDesc());
					});
		}
	}

	@Nested
	class createUser {
		private UserSignUpRequest request;

		@BeforeEach
		void setUpCreateUserTest() {
			request = new UserSignUpRequest();
			request.setUsername("Devonic");
			request.setPassword("Password09");
			request.setEmail("Devonic@gmail.com");
			request.setNik("2345678912345678");
			request.setFirstName("Devonic");
			request.setLastName("Nico");
			request.setNoTelp("085889170242");
			request.setAddress("Jakarta");
			request.setDob("30-12-1995");

			when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());
			when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(new Customer());

			when(userRepository.findByUsername("Devonic")).thenReturn(Optional.empty());
			when(userRepository.findByEmail("Devonic@gmail.com")).thenReturn(Optional.empty());
		}

		@Test
		void createUserSuccessDefault() throws Exception {
			mockMvc.perform(post("/api/user/signUp").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON).header("Accept-Language", "id")
					.header("Authorization", "Bearer " + token).content(mapper.writeValueAsString(request)))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.SUCCESS.getCode(), response.getStatusCode());
					});
		}
		
		@Test
		void createUserSuccessAdmin() throws Exception {
			request.setUserRole(1L);
			mockMvc.perform(post("/api/user/signUp").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON).header("Accept-Language", "id")
					.header("Authorization", "Bearer " + token).content(mapper.writeValueAsString(request)))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.SUCCESS.getCode(), response.getStatusCode());
					});	
		}

		@Test
		void createUserFailExistData() throws Exception {
			request.setUsername("Kielvien");

			mockMvc.perform(post("/api/user/signUp").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON).header("Accept-Language", "id")
					.header("Authorization", "Bearer " + token).content(mapper.writeValueAsString(request)))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.INVALID_DATA.getCode(), response.getStatusCode());
					});

			request.setUsername("Kielvien");
			request.setEmail("for2university@gmail.com");

			mockMvc.perform(post("/api/user/signUp").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON).header("Accept-Language", "id")
					.header("Authorization", "Bearer " + token).content(mapper.writeValueAsString(request)))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.INVALID_DATA.getCode(), response.getStatusCode());
					});

		}

		@Test
		void createUserFailSpaceUsername() throws Exception {
			request.setUsername(" testingJ unit5 ");

			mockMvc.perform(post("/api/user/signUp").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON).header("Accept-Language", "id")
					.header("Authorization", "Bearer " + token).content(mapper.writeValueAsString(request)))
					.andExpect(status().isOk()).andDo(resultHandler -> {
						GeneralResponses<UserResponse> response = mapper
								.readValue(resultHandler.getResponse().getContentAsString(), new TypeReference<>() {
								});

						assertNotNull(response);
						assertNotNull(response.getDesc());
						assertNull(response.getData());

						assertEquals(Constants.HttpStatusCode.INVALID_DATA.getCode(), response.getStatusCode());
					});
		}
	}
}
