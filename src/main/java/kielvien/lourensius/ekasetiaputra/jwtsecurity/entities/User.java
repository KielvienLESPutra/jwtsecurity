package kielvien.lourensius.ekasetiaputra.jwtsecurity.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "user")
@Entity
public class User extends AuditTrail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	@Column(unique = true)
	private String username;
	private String password;
	private String email;
	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "last_login")
	private Date lastLogin;

	@Column(name = "last_attempt")
	private Date lastAttempt;

	@Column(name = "fail_login_count")
	private int failLoginCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
//	@JsonBackReference
	private Customer customer;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(Date lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

	public int getFailLoginCount() {
		return failLoginCount;
	}

	public void setFailLoginCount(int failLoginCount) {
		this.failLoginCount = failLoginCount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
