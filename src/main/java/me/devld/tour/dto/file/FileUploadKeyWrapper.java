package me.devld.tour.dto.file;

public class FileUploadKeyWrapper {

    private String service;

    private Object opts;

    public FileUploadKeyWrapper(String service) {
        this.service = service;
    }

    public FileUploadKeyWrapper(String service, Object opts) {
        this.service = service;
        this.opts = opts;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Object getOpts() {
        return opts;
    }

    public void setOpts(Object opts) {
        this.opts = opts;
    }
}
