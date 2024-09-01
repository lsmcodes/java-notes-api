package io.github.lsmcodes.notes_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Generic Server")})
@EnableCaching
public class NotesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesApiApplication.class, args);
	}

}