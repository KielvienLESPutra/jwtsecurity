package kielvien.lourensius.ekasetiaputra.jwtsecurity.configuration;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggerAspect {
	private final Logger log = LoggerFactory.getLogger(LoggerAspect.class);
	private static final String TRACING_KEY = "tracingTransactionId";
	private final ObjectMapper mapper = new ObjectMapper();

	@Pointcut("execution(* kielvien.lourensius.ekasetiaputra.jwtsecurity.controllers..*(..)) ||"
			+ "execution(* kielvien.lourensius.ekasetiaputra.jwtsecurity.services..*(..))")
	public void executionMethodPoint() {

	}

//	NOSONAR
//	@Before("executionMethodPoint()")
//	public void beforeJoinExecuted(JoinPoint joinPoint) {
//		String tracingId = MDC.get(TRACING_KEY);
//		if (tracingId == null) {
//			tracingId = UUID.randomUUID().toString();
//			MDC.put(TRACING_KEY, tracingId);
//			log.info("Generate And Put Tracing Key: {}", tracingId);
//		}
//		log.info("Entaring tracing id [{}] executor point {} with parameter {}", tracingId, joinPoint.getSignature(),
//				joinPoint.getArgs());
//	}
//
//	@After("executionMethodPoint()")
//	public void afterJoinExecuted(JoinPoint joinPoint) {
//		String tracingId = MDC.get(TRACING_KEY);
//		log.info("Exiting tracing id [{}] executor point {} with parameter {}", tracingId, joinPoint.getSignature(),
//				joinPoint.getArgs());
//	}

	@Around("executionMethodPoint()")
	public Object aroundJoinExecuted(ProceedingJoinPoint joinPoint) throws Throwable {
		String tracingId = MDC.get(TRACING_KEY);
		boolean isRootTracing = false;
		Object result = joinPoint.proceed();
		String resultAsString = "";
		
		if (tracingId == null) {
			tracingId = UUID.randomUUID().toString();
			MDC.put(TRACING_KEY, tracingId);
			isRootTracing = true;
			log.info("Generate And Put Tracing Key: {}", tracingId);
		}
		
		try {
			resultAsString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			log.warn("Fail to decode result tracing id [{}] from point {} ", tracingId, joinPoint.getSignature());
		} finally {
			log.info("Tracing id [{}] executor point {} with parameter {} result {}", tracingId,
					joinPoint.getSignature(), joinPoint.getArgs(), resultAsString);
			if (isRootTracing) {
				log.info("Removing Tracing Key : {}", tracingId);
				MDC.remove(TRACING_KEY);
			}
		}

		return result;
	}
}
