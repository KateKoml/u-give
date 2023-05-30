package com.ugive.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "U-Give DEMO Project",
                version = "1.0",
                description = "Trading Platform",
                contact = @Contact(
                        name = "Ekaterina Komleva",
                        email = "komlevakate99@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "https://opensource.org/licenses/mit-license.php"
                )
        ),
        security = {
                @SecurityRequirement(name = "authToken")
        }
)
@SecuritySchemes(value = {
        @SecurityScheme(name = "authToken",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER,
                paramName = "Authorization",
                description = "Token for JWT Authentication")
})

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }
//
//    private Info apiInfo() {
//        return new Info()
//                .contact(contact())
//                .title("U-Give DEMO Project")
//                .description("Trading Platform")
//                .version("1.0")
//                .license(apiLicence());
//    }
//
//    private License apiLicence() {
//        return new License()
//                .name("MIT Licence")
//                .url("https://opensource.org/licenses/mit-licenses.php");
//    }
//
//    private Contact contact() {
//        return new Contact()
//                .email("komlevakate99@gmail.com")
//                .name("Ekaterina Komleva")
//                .url("https://u-give.com");
//    }
}
