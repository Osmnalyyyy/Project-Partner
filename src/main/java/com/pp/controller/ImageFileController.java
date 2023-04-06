package com.pp.controller;

import com.pp.domain.ImageFile;
import com.pp.dto.ImageFileDTO;
import com.pp.dto.response.PPResponse;
import com.pp.dto.response.ImageSaveResponse;
import com.pp.dto.response.ResponseMessage;
import com.pp.service.ImageFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/image")
@AllArgsConstructor
public class ImageFileController {

    private ImageFileService imageFileService;

                    /*UPLOAD*/
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ImageSaveResponse> uploadFile(@RequestParam("image") MultipartFile image){
       String imageId = imageFileService.saveImage(image);
       ImageSaveResponse response=new ImageSaveResponse
               (imageId,ResponseMessage.IMAGE_SAVE_RESPONSE_MESSAGE,true);

       return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable String id){

        ImageFile imageFile=imageFileService.getImageById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+imageFile.getName()).body(imageFile.getImageData().getData());

    }
                    /*İMAGE DİSPLAY*/

    @GetMapping("/display/{id}")

    public ResponseEntity<byte[]>displayImage(@PathVariable String id){
        ImageFile imageFile= imageFileService.getImageById(id);
        HttpHeaders header=new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageFile.getImageData().getData(), header, HttpStatus.OK);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages(){
        List<ImageFileDTO> imageList=imageFileService.getAllImages();
        return ResponseEntity.status(HttpStatus.OK).body(imageList);
    }

                     /*Delete İMAGE*/

   @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<PPResponse>deleteImageFile(@PathVariable String id){
        imageFileService.removeById(id);
        PPResponse response =new PPResponse(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE,true);
                return ResponseEntity.ok(response);
    }



}
