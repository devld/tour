package me.devld.tour.dto.file;

import java.util.Collections;
import java.util.List;

public enum FileType {

    IMAGE(Collections.singletonList("image/*"));

    private final List<String> mimeType;

    FileType(List<String> mimeType) {
        this.mimeType = mimeType;
    }

    public List<String> getMimeType() {
        return mimeType;
    }
}
