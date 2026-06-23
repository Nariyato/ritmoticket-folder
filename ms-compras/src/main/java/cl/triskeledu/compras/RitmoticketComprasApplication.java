package cl.triskeledu.compras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RitmoticketComprasApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketComprasApplication.class, args);
	}

}
