package com.hello;

import de.dentrassi.crypto.pem.PemKeyStoreProvider;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.Security;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication
@EntityScan(basePackages = {"com.hello.dbservices.entity"})
public class Application {
    public static void main(String[] args) {
//        Security.addProvider(new PemKeyStoreProvider());
        SpringApplication.run(Application.class, args);
    }
}
