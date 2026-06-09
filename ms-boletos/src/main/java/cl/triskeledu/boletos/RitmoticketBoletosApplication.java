package cl.triskeledu.boletos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RitmoticketBoletosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketBoletosApplication.class, args);
	}

}
