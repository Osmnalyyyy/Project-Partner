package com.pp.exception.message;

public class AttachmentFileException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AttachmentFileException(String message) {
        super(message);
    }
}
