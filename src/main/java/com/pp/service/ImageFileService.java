package com.pp.service;

import com.pp.domain.ImageData;
import com.pp.domain.ImageFile;
import com.pp.dto.ImageFileDTO;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.ErrorMessage;
import com.pp.exception.message.ImageFileException;
import com.pp.repository.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public String saveImage(MultipartFile file) {

        ImageFile imageFile=null;

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try
        {
            ImageData imageData = new ImageData(file.getBytes());
            imageFile=new ImageFile(fileName,file.getContentType(), imageData);

        }catch (IOException e){
            throw new ImageFileException(e.getMessage());
        }
        imageFileRepository.save(imageFile);
        return  imageFile.getId();
    }

    public ImageFile getImageById(String id) {

        return imageFileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));
    }

    public List<ImageFileDTO> getAllImages() {
        List<ImageFile> imageList = imageFileRepository.findAll();
        return imageList.stream().map(imgFile ->{
           String imageUri= ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/image/download/")
                    .path(imgFile.getId())
                    .toUriString();
            return new ImageFileDTO(imgFile.getName(),imageUri,imgFile.getType());
        } ).collect(Collectors.toList());
    }


    public void removeById(String id) {

        ImageFile imageFile =  getImageById(id);
        imageFileRepository.delete(imageFile);
    }

    public ImageFile findImageById(String id) {
        return imageFileRepository.findImageById(id).orElseThrow(()-> new
                ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));
    }

}
