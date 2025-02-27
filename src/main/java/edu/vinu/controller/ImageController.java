package edu.vinu.controller;

import edu.vinu.dto.ImageDto;
import edu.vinu.model.Image;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId){
        try {
            List<ImageDto> images = imageService.saveImage(files,productId);
            return ResponseEntity.ok(new ApiResponse("Upload Successful!", images));
        } catch (java.lang.Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed!",e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
            Image image=imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImg().getBytes(1,(int) image.getImg().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getFileName()+"\"")
                    .body(resource);
    }
    @PutMapping("image/{imageId}/update")
    public  ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("Image updated successfully!",null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed Due To Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("image/{imageId}/delete")
    public  ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Image deleted successfully!",null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed Due To Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
