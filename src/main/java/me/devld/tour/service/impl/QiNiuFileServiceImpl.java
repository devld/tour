package me.devld.tour.service.impl;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import me.devld.tour.config.AppConfig;
import me.devld.tour.dto.file.FileType;
import me.devld.tour.dto.file.FileUploadIn;
import me.devld.tour.dto.file.FileUploadKeyWrapper;
import me.devld.tour.dto.file.QiniuFileUploadKey;
import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.exception.BadRequestException;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.service.FileService;
import me.devld.tour.service.UserService;
import me.devld.tour.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class QiNiuFileServiceImpl implements FileService {

    private final AppConfig.QiNiuFileConfig config;
    private final Auth auth;

    private final UserService userService;

    public QiNiuFileServiceImpl(AppConfig appConfig, UserService userService) {
        this.config = appConfig.getQiNiuFileConfig();
        this.userService = userService;
        this.auth = Auth.create(config.getAccessKey(), config.getSecretKey());
    }

    @Override
    public FileUploadKeyWrapper prepareUpload(FileUploadIn fileUpload, long userId) {
        if (fileUpload.getType() == null || StringUtils.isEmpty(fileUpload.getFilename())) {
            throw new BadRequestException();
        }
        FileType fileType = fileUpload.getType();
        String filename = fileUpload.getFilename();
        if (config.getMaxFileSize() > 0 && fileUpload.getFileSize() != null && fileUpload.getFileSize() > config.getMaxFileSize()) {
            throw new ForbiddenException("msg.file_size_too_large");
        }
        String fileKey = config.getFilePrefix() + fileType.name().toLowerCase() + "/" + UUID.randomUUID().toString().replace("-", "") +
                "." +
                StringUtils.getFilenameExtension(fileUpload.getFilename());
        StringMap policy = new StringMap();
        policy.put("insertOnly", 1);
        if (config.getMaxFileSize() > 0) {
            policy.put("fsizeLimit", config.getMaxFileSize());
        }
        policy.put("mimeLimit", String.join(";", fileType.getMimeType()));
        String persistentOps = getPersistentOps(fileKey, fileUpload, userId);
        if (!StringUtils.isEmpty(persistentOps)) {
            policy.put("persistentOps", persistentOps);
        }
        QiniuFileUploadKey fileUploadKey = new QiniuFileUploadKey(auth.uploadToken(config.getBucketName(), fileKey, 3600L, policy), fileKey, config.getBaseUrl());
        return new FileUploadKeyWrapper("qiniu", fileUploadKey);
    }

    private String getPersistentOps(String fileKey, FileUploadIn fileUpload, long userId) {
        if (StringUtils.isEmpty(fileUpload.getOptions().get("wm"))) {
            return null;
        }
        UserProfile user = userService.getUserInfo(userId);
        // 添加 昵称/@用户名 的水印
        return "imageView2/0/q/75|watermark/2/text/" +
                TextUtils.base64Encode(user.getNickname() + "/@" + user.getUsername()) +
                "/font/bWFsZ3VuIGdvdGhpYw==/fontsize/480/fill/IzY4RDZCQw==/dissolve/65/gravity/SouthEast/dx/10/dy/10|imageslim|saveas/" +
                TextUtils.base64Encode(config.getBucketName() + ":" + fileKey);

    }

}
