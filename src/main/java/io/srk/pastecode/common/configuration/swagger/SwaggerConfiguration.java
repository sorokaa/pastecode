package io.srk.pastecode.common.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Components components() {
        return new Components();
    }

    @Bean
    public OpenAPI customOpenAPI(Components components) {
        Info apiInfo = new Info().title("Pastecode API");
        return new OpenAPI()
                .components(components)
                .info(apiInfo);
    }
}
