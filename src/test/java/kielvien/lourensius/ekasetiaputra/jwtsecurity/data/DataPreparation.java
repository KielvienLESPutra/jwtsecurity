package kielvien.lourensius.ekasetiaputra.jwtsecurity.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.Customer;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.User;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserRolePermission;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.utils.Util;

public class DataPreparation {
	private User user;
	private Customer customer;
	private List<UserRolePermission> listUserRolePermissions;
	
	public DataPreparation() throws ParseException {
		user = new User();
		user.setUserId(1L);
		user.setActive(true);
		user.setUsername("Kielvien");
		user.setPassword("$2a$10$MljSZ6GoKqMRyG6kTXz9Te8JiMtLOiWBA8q7SfqP9bT0hwTKYouRm");
		user.setEmail("for2university@gmail.com");
		user.setActive(true);
		user.setFailLoginCount(0);
		user.setLastAttempt(null);
		user.setLastLogin(null);
		
		customer = new Customer();
		customer.setCustomerId(1L);
		customer.setUserId(1L);
		customer.setNik("1234567891234567");
		customer.setFirstName("Kielvien");
		customer.setLastName("Lourensius");
		customer.setNoTelp("085889170242");
		customer.setAlternatifNoTelp(null);
		customer.setAddress("Jakarta");
		customer.setDob(Util.changeDateFormatYMD("30-11-1994"));

		customer.getUser().add(user);
		user.setCustomer(customer);
		
		listUserRolePermissions = new ArrayList<UserRolePermission>();
		UserRolePermission getPermission = new UserRolePermission();
		getPermission.setPermissionId(1L);
		getPermission.setPermissionName("GET_USER");
		getPermission.setRoleId(1L);
		getPermission.setRoleName("ADMIN");
		getPermission.setUserId(1L);
		listUserRolePermissions.add(getPermission);

		UserRolePermission createPremission = new UserRolePermission();
		createPremission.setPermissionId(2L);
		createPremission.setPermissionName("CREATE_USER");
		createPremission.setRoleId(1L);
		createPremission.setRoleName("ADMIN");
		createPremission.setUserId(1L);
		listUserRolePermissions.add(createPremission);
	}

	public User getUser() {
		return user;
	}

	public Customer getCustomer() {
		return customer;
	}

	public List<UserRolePermission> getListUserRolePermissions() {
		return listUserRolePermissions;
	}
}
