package com.ugive.config;

public interface JwtConfigProvider {
    String getSecret();

    Integer getExpiration();

    String getPasswordSalt();
}
