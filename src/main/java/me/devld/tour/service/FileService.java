package me.devld.tour.service;

import me.devld.tour.dto.file.FileUploadIn;

public interface FileService {

    Object prepareUpload(FileUploadIn fileUpload, long userId);

}
