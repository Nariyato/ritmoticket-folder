package cl.triskeledu.reportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RitmoticketReportesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketReportesApplication.class, args);
	}

}
