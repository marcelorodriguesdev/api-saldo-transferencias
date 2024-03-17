package br.com.itau.apisaldotransferencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan
public class ApiSaldoTransferenciasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSaldoTransferenciasApplication.class, args);
	}

}
