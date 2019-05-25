package me.devld.tour.config;

import me.devld.tour.dto.poi.PoiType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableConfigurationProperties(AppConfig.class)
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfig {

    private ApiTokenConfig apiToken;

    private QiNiuFileConfig qiNiuFileConfig;

    private AMapConfig aMapConfig;

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

    public AMapConfig getaMapConfig() {
        return aMapConfig;
    }

    public void setaMapConfig(AMapConfig aMapConfig) {
        this.aMapConfig = aMapConfig;
    }

    public static class AMapConfig {
        private String apiKey;
        private Map<PoiType, String> poiCode;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public Map<PoiType, String> getPoiCode() {
            return poiCode;
        }

        public void setPoiCode(Map<PoiType, String> poiCode) {
            this.poiCode = poiCode;
        }
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
