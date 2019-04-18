package me.devld.tour.service;

import me.devld.tour.dto.file.FileType;

public interface FileService {

    Object prepareUpload(FileType fileType, String filename, long fileSize);

}
