package br.com.luizmatosdev.desafioveiculos.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiConfig {
    private String url;
    private Duration timeout = Duration.ofSeconds(30);
}
