package com.pixel.synchronre.archivemodule.model.dtos.request;

import com.pixel.synchronre.archivemodule.model.dtos.validator.ValidDocType;
import com.pixel.synchronre.archivemodule.model.dtos.validator.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ValidFileExtension
public class UploadDocReq
{
    /*@ExistingUserId(groups = {OnUSerUpload.class})
    @ExistingRepId(groups = {OnRegUpload.class})
    @ExistingPlaId(groups = {OnPlaUpload.class})
    @ExistingAffId(groups = {OnAffUpload.class})*/
    private Long objecId;
    //@ValidDocType
    private String docUniqueCode;
    private String docNum;
    private String docDescription;
    //@ValidFileSize
    private MultipartFile file;
}
