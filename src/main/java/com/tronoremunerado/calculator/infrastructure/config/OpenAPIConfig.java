package com.tronoremunerado.calculator.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class OpenAPIConfig {

    @Value("${paid.throne.dev-url:http://localhost:8080}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        log.debug("Configurando OpenAPI/Swagger para a aplicação");
        log.debug("URL do servidor de desenvolvimento: {}", devUrl);

        Server devServer = new Server()
                .url(devUrl)
                .description("Development environment");

        Contact contact = new Contact()
                .name("Paid Throne Team")
                .email("contact@paidthrone.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Paid Throne Calculator API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to calculate how much money you earn while in the bathroom." +
                        "\n\nFeatures include:" +
                        "\n* Calculate earnings based on bathroom time" +
                        "\n* Support for different salary types" +
                        "\n* Validation of maximum bathroom time" +
                        "\n* Detailed work schedule handling")
                .license(mitLicense);

        log.debug("OpenAPI configurado com sucesso - Título: {}, Versão: {}", info.getTitle(), info.getVersion());
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
