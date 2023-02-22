package com.ticc.webapiservice.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JwtConfig {
    @Bean
    KeyPair generateKeyPair()
    {
        log.info("Generating key pair");
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    @Bean
    public RSAPrivateKey privateKey(KeyPair keyPair)
    {
        try {
            return(RSAPrivateKey) keyPair.getPrivate();
        } catch (Exception e) {
            log.error("Error while getting private key",e);
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public RSAPublicKey publicKey(KeyPair keyPair)
    {
        try {
            return (RSAPublicKey) keyPair.getPublic();
        } catch (Exception e) {
            log.error("Error while getting public key", e);
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey)
    {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
