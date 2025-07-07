package kielvien.lourensius.ekasetiaputra.jwtsecurity.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.Customer;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.RoleUser;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.User;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.DataNotFoundExecption;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.FailLoginException;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.InvalidDataException;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserResponse;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserRolePermission;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserSignUpRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.CustomerRepository;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.RoleUserRespository;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.repository.UserRepository;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.utils.SecurityUtil;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.utils.Util;

@Service
public class UserServices {

	private Logger log = LoggerFactory.getLogger(UserServices.class);
	private final CustomerRepository customerRepository;
	
	@Value("${maximum.attempt.login}")
	private int maxAttempt;
	
	private UserRepository userRepository;
	private RoleUserRespository roleUserRespository;
	private SecurityUtil securityUtil;

	public UserServices(CustomerRepository customerRepository, UserRepository userRepository,
			RoleUserRespository roleUserRespository, SecurityUtil securityUtil) {
		this.customerRepository = customerRepository;
		this.userRepository = userRepository;
		this.roleUserRespository = roleUserRespository;
		this.securityUtil = securityUtil;
	}

	public UserResponse getUser(Long id) throws DataNotFoundExecption {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			User data = user.get();

			UserResponse userResponse = new UserResponse();
			userResponse.setUsername(data.getUsername());
			userResponse.setActive(data.isActive());
			userResponse.setLastLogin(data.getLastLogin());
			userResponse.setAddress(data.getCustomer().getAddress());
			userResponse.setBod(Util.changeDateFormatYMD(data.getCustomer().getDob()));
			userResponse.setFirstName(data.getCustomer().getFirstName());
			userResponse.setLastName(data.getCustomer().getLastName());

			return userResponse;
		} else {
			throw new DataNotFoundExecption("Data User Not Found For ID : " + id);
		}
	}

	@Transactional
	public void signtUp(UserSignUpRequest userSignUpRequest) throws ParseException, InvalidDataException {
		if (Util.checkUsername(userSignUpRequest.getUsername())) {
			log.info("Invalid Username Have Space !!");
			throw new InvalidDataException("Invalid data username : " + userSignUpRequest.getUsername());
		}

		if (searchUserByUsername(userSignUpRequest.getUsername()).isPresent()
				|| searchUserByEmail(userSignUpRequest.getEmail()).isPresent()) {
			log.info("Invalid Data Already Exist !!");
			throw new InvalidDataException("Invalid data username or email : " + userSignUpRequest.getUsername()
					+ " ,emal : " + userSignUpRequest.getEmail());
		}
		User user = new User();
		user.setUsername(userSignUpRequest.getUsername());
		user.setPassword(securityUtil.encryptPassword(userSignUpRequest.getPassword()));
		if (!userSignUpRequest.getEmail().isEmpty()) {
			user.setEmail(userSignUpRequest.getEmail());
		}
		user.setFailLoginCount(0);
		user.setActive(true);

		Customer customer = new Customer();
		if (!userSignUpRequest.getNik().isEmpty()) {
			customer.setNik(userSignUpRequest.getNik());
		}
		customer.setFirstName(userSignUpRequest.getFirstName());
		customer.setLastName(userSignUpRequest.getLastName());
		customer.setNoTelp(userSignUpRequest.getNoTelp());
		customer.setAlternatifNoTelp(userSignUpRequest.getAlternatifNoTelp());
		if (!userSignUpRequest.getAddress().isEmpty()) {
			customer.setAddress(userSignUpRequest.getAddress());
		}
		customer.setDob(Util.changeDateFormatYMD(userSignUpRequest.getDob()));
		customerRepository.save(customer);

		RoleUser roleUser = new RoleUser();
		roleUser.setUserId(user.getUserId());
		if (userSignUpRequest.getUserRole() == null) {
			roleUser.setUserRoleId(Constants.ROLE_USER_DEFAULT);
		} else {
			roleUser.setUserRoleId(userSignUpRequest.getUserRole());
		}
		customer.getUser().add(user);

		roleUserRespository.save(roleUser);
	}

	public User searchByIndetifier(String identifier) throws FailLoginException {
		if (Util.checkIsEmail(identifier)) {
			return searchUserByEmail(identifier).orElseThrow(
					() -> new FailLoginException("Data user search not found with email : " + identifier));
		} else {
			return searchUserByUsername(identifier).orElseThrow(
					() -> new FailLoginException("Data user search not found with username : " + identifier));
		}
	}

	private Optional<User> searchUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	private Optional<User> searchUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<List<UserRolePermission>> searchUserRolePermission(Long userId) {
		return userRepository.findUserWihRolesAndPermissions(userId);
	}

	@Transactional
	public void updateLastLogin(User user, boolean isFail) {
		if (!isFail) {
			user.setLastLogin(new Date());
			userRepository.save(user);
			return;
		} 
		
		user.setLastAttempt(new Date());
		user.setFailLoginCount(user.getFailLoginCount() + 1);
		
		if(user.getFailLoginCount() > maxAttempt) {
			user.setActive(false);
		}
		userRepository.save(user);
	}
}