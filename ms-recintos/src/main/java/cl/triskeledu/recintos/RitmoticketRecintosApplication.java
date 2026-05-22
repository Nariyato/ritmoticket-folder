package cl.triskeledu.recintos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.triskeledu.recintos.client")
@SpringBootApplication
public class RitmoticketRecintosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketRecintosApplication.class, args);
	}

}
