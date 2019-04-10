package me.devld.tour.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(AppConfig.class)
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfig {

    private ApiTokenConfig apiToken;

    private FileConfig file;

    public FileConfig getFile() {
        return file;
    }

    public void setFile(FileConfig file) {
        this.file = file;
    }

    public ApiTokenConfig getApiToken() {
        return apiToken;
    }

    public void setApiToken(ApiTokenConfig apiToken) {
        this.apiToken = apiToken;
    }

    public static class FileConfig {
        private String path;
        private String imageDir;
        private String imageBasePath = "/image";

        public String getImageDir() {
            return imageDir;
        }

        public void setImageDir(String imageDir) {
            this.imageDir = imageDir;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getImageBasePath() {
            return imageBasePath;
        }

        public void setImageBasePath(String imageBasePath) {
            this.imageBasePath = imageBasePath;
        }
    }

    public static class ApiTokenConfig {
        private String secret;
        private int serverInteger;
        private long validity;

        private String cookieKey;

        public String getCookieKey() {
            return cookieKey;
        }

        public void setCookieKey(String cookieKey) {
            this.cookieKey = cookieKey;
        }

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
