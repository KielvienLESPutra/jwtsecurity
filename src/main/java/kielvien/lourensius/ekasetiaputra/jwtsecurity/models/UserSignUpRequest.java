package kielvien.lourensius.ekasetiaputra.jwtsecurity.models;

import jakarta.validation.constraints.NotBlank;

public class UserSignUpRequest {
	@NotBlank(message = "{validation.username}")
	private String username;
	@NotBlank(message = "{validation.password}")
	private String password;
	@NotBlank(message = "{validation.secondPassword}")
	private String secondPassword;
	@NotBlank(message = "{validation.email}")
	private String email;
	private String nik;
	@NotBlank(message = "{validation.firstName}")
	private String firstName;
	@NotBlank(message = "{validation.lastName}")
	private String lastName;
	@NotBlank(message = "{validation.noTelp}")
	private String noTelp;
	private String alternatifNoTelp;
	private String address;
	@NotBlank(message = "{validation.dob}")
	private String dob;
	private Long userRole;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecondPassword() {
		return secondPassword;
	}

	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNoTelp() {
		return noTelp;
	}

	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}

	public String getAlternatifNoTelp() {
		return alternatifNoTelp;
	}

	public void setAlternatifNoTelp(String alternatifNoTelp) {
		this.alternatifNoTelp = alternatifNoTelp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Long getUserRole() {
		return userRole;
	}

	public void setUserRole(Long userRole) {
		this.userRole = userRole;
	}
}
