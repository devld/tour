package me.devld.tour.controller.web;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.file.FileType;
import me.devld.tour.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/prepare")
    public Object getFileUploadKey(@RequestParam("type") FileType type, @RequestParam("name") String filename, @RequestParam("size") long fileSize) {
        return fileService.prepareUpload(type, filename, fileSize);
    }

}
