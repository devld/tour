package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.ApiResult;
import me.devld.tour.dto.ImageFileType;
import me.devld.tour.service.ImageFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@ApiController
@RequestMapping("/image")
public class ImageController {

    private final ImageFileService imageFileService;

    public ImageController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    @PostMapping("/upload")
    public ApiResult uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("type") ImageFileType type) {
        return ApiResult.ok(imageFileService.saveFile(file, type));
    }

}
