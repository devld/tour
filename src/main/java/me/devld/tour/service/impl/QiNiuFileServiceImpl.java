package me.devld.tour.service.impl;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import me.devld.tour.config.AppConfig;
import me.devld.tour.dto.file.FileType;
import me.devld.tour.dto.file.FileUploadKey;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class QiNiuFileServiceImpl implements FileService {

    private final AppConfig.QiNiuFileConfig config;
    private final Auth auth;

    public QiNiuFileServiceImpl(AppConfig appConfig) {
        this.config = appConfig.getQiNiuFileConfig();
        this.auth = Auth.create(config.getAccessKey(), config.getSecretKey());
    }

    @Override
    public FileUploadKey prepareUpload(FileType fileType, String filename, long fileSize) {
        if (config.getMaxFileSize() > 0 && fileSize > config.getMaxFileSize()) {
            throw new ForbiddenException("msg.file_size_too_large");
        }
        String fileKey = config.getFilePrefix() + fileType.name().toLowerCase() + "/" + UUID.randomUUID().toString().replace("-", "") +
                "." +
                StringUtils.getFilenameExtension(filename);
        StringMap policy = new StringMap();
        policy.put("insertOnly", 1);
        if (config.getMaxFileSize() > 0) {
            policy.put("fsizeLimit", config.getMaxFileSize());
        }
        policy.put("mimeLimit", String.join(";", fileType.getMimeType()));
        return new FileUploadKey(auth.uploadToken(config.getBucketName(), fileKey, 3600L, policy), fileKey);
    }

}
