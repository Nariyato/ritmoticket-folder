package cl.triskeledu.pagos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RitmoticketPagosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketPagosApplication.class, args);
	}

}
