package cart.project.cartApi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket getBean() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .paths(PathSelectors.regex("/cart-api/.*"))
                .build()
                .apiInfo(getInfo());
    }
    private ApiInfo getInfo() {
        return new ApiInfoBuilder()
                .title("Cart API")
                .description("Cart API for delivery app")
                .build();
    }
}
