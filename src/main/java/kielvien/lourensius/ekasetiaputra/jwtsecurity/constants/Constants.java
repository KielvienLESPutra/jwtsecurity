package kielvien.lourensius.ekasetiaputra.jwtsecurity.constants;

public final class Constants {
	private Constants() {
		throw new UnsupportedOperationException("Constants class");
	}

	public enum HttpStatusCode {
		SUCCESS("00", "Success"),
		DATA_NOT_FOUND("01", "Data Not Found"),
		FAILED_LOGIN("02", "Fail Login"),
		INVALID_DATA("03", "Invalid Data Login"),;

		private String code;
		private String desc;

		private HttpStatusCode(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static final String CREATED_BY_SYSTEM = "SYSTEM";
	public static final Long ROLE_USER_DEFAULT = 2L;
}
