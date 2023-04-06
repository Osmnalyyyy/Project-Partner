package com.pp.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ImageSaveResponse extends PPResponse{
    private String imageId;

   public ImageSaveResponse(String imageId, String message, boolean success) {
       super(message, success);
    this.imageId=imageId;
   }


}
