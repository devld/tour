package me.devld.tour.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;

import java.security.SecureRandom;

public class ApiTokenService extends KeyBasedPersistenceTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTokenService.class);

    private final long validity;

    public ApiTokenService(String serverSecret, int serverInteger, SecureRandom secureRandom, long validity) {
        this.validity = validity;
        setServerSecret(serverSecret);
        setServerInteger(serverInteger);
        setSecureRandom(secureRandom);
    }

    @Override
    public Token verifyToken(String key) {
        Token token;
        try {
            token = super.verifyToken(key);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("illegal token received.", e);
            return null;
        }
        if (token != null) {
            long creation = token.getKeyCreationTime();
            if ((creation + validity) < System.currentTimeMillis()) {
                // expired
                return null;
            }
        }
        return token;
    }
}
