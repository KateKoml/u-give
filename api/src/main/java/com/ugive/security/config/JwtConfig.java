package com.ugive.security.config;

import com.ugive.config.JwtConfigProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "config")
public class JwtConfig implements JwtConfigProvider {

    private String secret;

    private Integer expiration;

    private String passwordSalt;

}