package cl.triskeledu.artistas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.triskeledu.artistas.client")
@SpringBootApplication
public class RitmoticketArtistasApplication {

	public static void main(String[] args) {
		SpringApplication.run(RitmoticketArtistasApplication.class, args);
	}	

}
