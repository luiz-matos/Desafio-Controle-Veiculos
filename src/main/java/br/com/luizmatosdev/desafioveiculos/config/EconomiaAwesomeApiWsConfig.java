package br.com.luizmatosdev.desafioveiculos.config;

import java.time.Duration;
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
    private Duration timeout = Duration.ofSeconds(30);
}
