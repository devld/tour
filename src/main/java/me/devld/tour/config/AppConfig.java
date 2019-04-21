package me.devld.tour.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(AppConfig.class)
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfig {

    private ApiTokenConfig apiToken;

    private QiNiuFileConfig qiNiuFileConfig;

    public ApiTokenConfig getApiToken() {
        return apiToken;
    }

    public void setApiToken(ApiTokenConfig apiToken) {
        this.apiToken = apiToken;
    }

    public QiNiuFileConfig getQiNiuFileConfig() {
        return qiNiuFileConfig;
    }

    public void setQiNiuFileConfig(QiNiuFileConfig qiNiuFileConfig) {
        this.qiNiuFileConfig = qiNiuFileConfig;
    }

    public static class QiNiuFileConfig {
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String baseUrl;
        private String filePrefix = "";
        private long maxFileSize = -1;

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getFilePrefix() {
            return filePrefix;
        }

        public void setFilePrefix(String filePrefix) {
            this.filePrefix = filePrefix;
        }

        public long getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
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
