package me.devld.tour.dto.file;

import me.devld.tour.util.MapUtil;

import java.util.Map;

public class FileUploadIn {

    private FileType type;
    private String filename;
    private Long fileSize;
    private Map<String, Object> options;

    public FileUploadIn(FileType type, String filename, Long fileSize, Object... options) {
        this.type = type;
        this.filename = filename;
        this.fileSize = fileSize;
        this.options = MapUtil.map(options);
    }

    public FileUploadIn(FileType type, String filename, Long fileSize, Map<String, Object> options) {
        this.type = type;
        this.filename = filename;
        this.fileSize = fileSize;
        this.options = options;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
