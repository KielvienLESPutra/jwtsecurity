package kielvien.lourensius.ekasetiaputra.jwtsecurity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private Util() {
		throw new UnsupportedOperationException("Util class");
	}

	private static final String PATTERN_SIMPLE_DATE = "dd-MM-yyyy";

	public static String changeDateFormatYMD(Date date) {
		return new SimpleDateFormat(PATTERN_SIMPLE_DATE).format(date);
	}

	public static Date changeDateFormatYMD(String date) throws ParseException {
		return new SimpleDateFormat(PATTERN_SIMPLE_DATE).parse(date);
	}

	public static boolean checkIsEmail(String indentifier) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return indentifier != null && indentifier.matches(emailRegex);
	}

	public static boolean checkUsername(String username) {
		String usernameRegex = ".*\s.*";
		return username != null && username.matches(usernameRegex);
	}
}
