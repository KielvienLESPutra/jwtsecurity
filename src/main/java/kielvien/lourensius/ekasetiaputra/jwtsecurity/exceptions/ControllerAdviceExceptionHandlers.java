package kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.GeneralResponses;

@ControllerAdvice
public class ControllerAdviceExceptionHandlers {
	private final MessageSource messageSource;

	public ControllerAdviceExceptionHandlers(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(DataNotFoundExecption.class)
	public ResponseEntity<Object> handlerDataNotFound(DataNotFoundExecption ex, Locale locale) {
		GeneralResponses<Object> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.DATA_NOT_FOUND.getCode());
		response.setDesc(messageSource.getMessage("exception.dataNotFound", null, locale));
		response.setData(null);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ExceptionHandler(FailLoginException.class)
	public ResponseEntity<Object> handlerFailedLogin(FailLoginException ex, Locale locale) {
		GeneralResponses<Object> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.FAILED_LOGIN.getCode());
		response.setDesc(messageSource.getMessage("exception.failLogin", null, locale));
		response.setData(null);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<Object> handlerDuplicateData(InvalidDataException ex, Locale locale) {
		GeneralResponses<Object> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.INVALID_DATA.getCode());
		response.setDesc(messageSource.getMessage("exception.invalidData", null, locale));
		response.setData(null);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Object> handlerTokenExpire(TokenExpiredException ex, Locale locale){
		GeneralResponses<Object> response = new GeneralResponses<>();
		response.setStatusCode(Constants.HttpStatusCode.INVALID_DATA.getCode());
		response.setDesc(messageSource.getMessage("exception.token", null, locale));
		response.setData(null);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
}
