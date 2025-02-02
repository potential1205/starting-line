package org.example.gogoma.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${domain.name}")
    private String domainName; // secret.properties에서 값 가져오기

    @Bean
    public OpenAPI openAPI() {
        Server httpsServer = new Server();
        httpsServer.setUrl(domainName);
        httpsServer.setDescription("Production HTTPS server");

        return new OpenAPI()
                .servers(List.of(httpsServer))  // https 서버를 서버 목록에 추가
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Swagger")
                .description("Specification")
                .version("1.0.0");
    }
}
