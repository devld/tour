package me.devld.tour.controller.web;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.file.FileType;
import me.devld.tour.dto.file.FileUploadIn;
import me.devld.tour.service.FileService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@ApiController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Request("上传文件(prepare)")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload/prepare")
    public Object getFileUploadKey(
            @RequestParam("type") FileType type,
            @RequestParam("name") String filename,
            @RequestParam("size") long fileSize,
            @RequestBody Map<String, Object> options) {
        return fileService.prepareUpload(
                new FileUploadIn(type, filename, fileSize, options),
                SecurityUtil.userId());
    }

}
