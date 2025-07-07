package kielvien.lourensius.ekasetiaputra.jwtsecurity.models;

public class AuthLoginResponse {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "AuthLoginResponse [token=" + token + "]";
	}
}
