package kielvien.lourensius.ekasetiaputra.jwtsecurity.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public SecurityUtil(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	public String encryptPassword(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

	public boolean checkPassword(String inputPassword, String password) {
		return bCryptPasswordEncoder.matches(inputPassword, password);
	}
}
