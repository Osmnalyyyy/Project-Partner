package com.pp.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttachmentSaveResponse extends PPResponse{
    private String attachmentId;

    public AttachmentSaveResponse(String attachmentId,String message,boolean success) {
        super(message, success);
        this.attachmentId=attachmentId;
    }
}
