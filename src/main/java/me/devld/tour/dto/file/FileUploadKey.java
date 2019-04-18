package me.devld.tour.dto.file;

public class FileUploadKey {

    private String token;

    private String key;

    public FileUploadKey(String token, String key) {
        this.token = token;
        this.key = key;
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
}
