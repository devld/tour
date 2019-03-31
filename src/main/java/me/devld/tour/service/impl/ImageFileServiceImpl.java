package me.devld.tour.service.impl;

import me.devld.tour.config.AppConfig;
import me.devld.tour.dto.ImageFileType;
import me.devld.tour.dto.image.ImageFileOut;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.ServerErrorException;
import me.devld.tour.service.ImageFileService;
import me.devld.tour.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageFileServiceImpl implements ImageFileService {

    private final AppConfig.FileConfig fileConfig;

    public ImageFileServiceImpl(AppConfig appConfig) {
        this.fileConfig = appConfig.getFile();
    }

    @Override
    public ImageFileOut saveFile(MultipartFile file, ImageFileType type) {
        String imageId = getImageId(file);
        try {
            file.transferTo(getImageFile(imageId, type));
        } catch (IOException e) {
            throw new ServerErrorException("msg.image_upload_failed", e);
        }
        return new ImageFileOut(type.name().toLowerCase() + "/" + imageId);
    }

    private File getImageFile(String imageId, ImageFileType type) throws IOException {
        File dir = new File(getImageRoot(), type.name().toLowerCase());
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IOException("cannot mkdir: " + dir.getAbsolutePath());
            }
        }
        return new File(dir, imageId);
    }

    private String getImageId(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (!TextUtils.isImageFilename(filename)) {
            throw new ForbiddenException("msg.invalid_image");
        }
        return UUID.randomUUID().toString().replace("-", "") +
                "." +
                StringUtils.getFilenameExtension(filename);
    }

    private File getImageRoot() throws IOException {
        File root = new File(fileConfig.getPath(), fileConfig.getImageDir());
        if (!root.exists()) {
            if (!root.mkdir()) {
                throw new IOException("cannot mkdir: " + root.getAbsolutePath());
            }
        }
        return root;
    }

}
