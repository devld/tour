package me.devld.tour.service;

import me.devld.tour.dto.ImageFileType;
import me.devld.tour.dto.image.ImageFileOut;
import org.springframework.web.multipart.MultipartFile;

public interface ImageFileService {

    ImageFileOut saveFile(MultipartFile file, ImageFileType type);

}
