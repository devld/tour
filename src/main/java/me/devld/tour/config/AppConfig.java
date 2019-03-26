package me.devld.tour.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(AppConfig.class)
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfig {

    private ApiTokenConfig apiToken;

    public ApiTokenConfig getApiToken() {
        return apiToken;
    }

    public void setApiToken(ApiTokenConfig apiToken) {
        this.apiToken = apiToken;
    }

    public static class ApiTokenConfig {
        private String secret;
        private int serverInteger;
        private long validity;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public int getServerInteger() {
            return serverInteger;
        }

        public void setServerInteger(int serverInteger) {
            this.serverInteger = serverInteger;
        }

        public long getValidity() {
            return validity;
        }

        public void setValidity(long validity) {
            this.validity = validity;
        }
    }

}
