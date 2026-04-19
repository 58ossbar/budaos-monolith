package com.budaos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tio.websocket.starter.EnableTioWebSocketServer;

@SpringBootApplication
@EnableTioWebSocketServer
public class BudaosIMApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudaosIMApplication.class, args);
	}

}
