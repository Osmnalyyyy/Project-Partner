package com.pp.controller;

import com.pp.domain.AttachmentFile;
import com.pp.domain.ImageFile;
import com.pp.dto.AttachmentFileDTO;
import com.pp.dto.response.AttachmentSaveResponse;
import com.pp.dto.response.PPResponse;
import com.pp.dto.response.ResponseMessage;
import com.pp.service.AttachmentFileService;
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
@RequestMapping("/attachment")
@AllArgsConstructor
public class AttachmentController {

    private final AttachmentFileService attachmentFileService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<AttachmentSaveResponse> uploadAttachment(@RequestParam("file") MultipartFile file){
        String attachmentId = attachmentFileService.saveAttachment(file);
        AttachmentSaveResponse response=new AttachmentSaveResponse
                (attachmentId, ResponseMessage.ATTACHMENT_SAVE_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getAttachmentFile(@PathVariable String id){

        AttachmentFile attachmentFile = attachmentFileService.getAttachmentById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+attachmentFile.getName()).body(attachmentFile.getAttachmentData().getData());

    }

    @GetMapping("/display/{id}")

    public ResponseEntity<byte[]>displayAttachment(@PathVariable String id){
        AttachmentFile attachment= attachmentFileService.getAttachmentById(id);
        HttpHeaders header=new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(attachment.getAttachmentData().getData(), header, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttachmentFileDTO>> getAllAttachments(){
        List<AttachmentFileDTO> attachmentFileDTO = attachmentFileService.getAllAttachments();
        return ResponseEntity.status(HttpStatus.OK).body(attachmentFileDTO);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<PPResponse>deleteAttachmentFile(@PathVariable String id){
        attachmentFileService.removeById(id);
        PPResponse response =new PPResponse(ResponseMessage.ATTACHMENT_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }
}
