package kielvien.lourensius.ekasetiaputra.jwtsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class JwtsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtsecurityApplication.class, args);
	}

}
