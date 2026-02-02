package io.github.samuel_pinheiro_c_lopes.serverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServerserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerserviceApplication.class, args);
	}

}
