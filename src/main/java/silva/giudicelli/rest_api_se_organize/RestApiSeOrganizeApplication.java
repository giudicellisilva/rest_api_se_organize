package silva.giudicelli.rest_api_se_organize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestApiSeOrganizeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiSeOrganizeApplication.class, args);
	}

}
