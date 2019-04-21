package me.devld.tour.dto.file;

public class FileUploadKey {

    private String token;

    private String key;

    private String baseUrl;

    public FileUploadKey(String token, String key, String baseUrl) {
        this.token = token;
        this.key = key;
        this.baseUrl = baseUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
