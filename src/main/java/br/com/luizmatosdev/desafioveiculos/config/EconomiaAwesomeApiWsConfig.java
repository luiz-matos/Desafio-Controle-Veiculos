package br.com.luizmatosdev.desafioveiculos.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "awesome.api")
public class EconomiaAwesomeApiWsConfig {
    private String url;
}
