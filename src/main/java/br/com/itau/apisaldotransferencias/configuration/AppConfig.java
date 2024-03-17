package br.com.itau.apisaldotransferencias.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.itau.apisaldotransferencias.core.domain.TransferenciaContext;

@Configuration
public class AppConfig {

    @Bean
    public TransferenciaContext transferenciaContext() {
        return new TransferenciaContext();
    }
}
