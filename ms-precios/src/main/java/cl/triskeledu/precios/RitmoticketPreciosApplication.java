package cl.triskeledu.precios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RitmoticketPreciosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketPreciosApplication.class, args);
	}

}
