package com.pp.service;

import com.pp.domain.AttachmentData;
import com.pp.domain.AttachmentFile;
import com.pp.domain.ImageData;
import com.pp.domain.ImageFile;
import com.pp.dto.AttachmentFileDTO;
import com.pp.dto.ImageFileDTO;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.AttachmentFileException;
import com.pp.exception.message.ErrorMessage;
import com.pp.exception.message.ImageFileException;
import com.pp.repository.AttachmentFileRepository;
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
public class AttachmentFileService {

    private final AttachmentFileRepository attachmentFileRepository;

    public String saveAttachment(MultipartFile file) {

        AttachmentFile attachmentFile = null;

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try
        {
            AttachmentData attachmentData = new AttachmentData(file.getBytes());
            attachmentFile=new AttachmentFile(fileName,file.getContentType(), attachmentData);

        }catch (IOException e){
            throw new AttachmentFileException(e.getMessage());
        }
        attachmentFileRepository.save(attachmentFile);
        return  attachmentFile.getId();
    }

    public AttachmentFile getAttachmentById(String id) {

        return attachmentFileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));
    }

    public List<AttachmentFileDTO> getAllAttachments() {
        List<AttachmentFile> attachmentFiles = attachmentFileRepository.findAll();
        return attachmentFiles.stream().map(atchFile ->{
            String attachmentUri= ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/attachment/download/")
                    .path(atchFile.getId())
                    .toUriString();
            return new AttachmentFileDTO(atchFile.getName(),attachmentUri,atchFile.getType());
        } ).collect(Collectors.toList());
    }


    public void removeById(String id) {

        AttachmentFile attachmentFile =  getAttachmentById(id);
        attachmentFileRepository.delete(attachmentFile);
    }

    public AttachmentFile findAttachmentById(String id) {
        return attachmentFileRepository.findAttachmentById(id).orElseThrow(()-> new
                ResourceNotFoundException(String.format(ErrorMessage.ATTACHMENT_NOT_FOUND_MESSAGE, id)));
    }
}
