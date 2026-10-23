package bo.quispe.edwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AclusersApplication {

	public static void main(String[] args) {
		SpringApplication.run(AclusersApplication.class, args);
	}

}
