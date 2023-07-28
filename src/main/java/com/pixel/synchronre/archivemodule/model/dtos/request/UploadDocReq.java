package com.pixel.synchronre.archivemodule.model.dtos.request;

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
    private Long objectId;
    //@ValidDocType
    private String docUniqueCode;
    private String docNum;
    private String docName;
    private String docDescription;
    //@ValidFileSize
    private MultipartFile file;
    private String base64UrlFile;
    private String extension;

    public UploadDocReq(Long objecId, String docUniqueCode, String docNum, String docName, String docDescription, MultipartFile file) {
        this.objectId = objecId;
        this.docUniqueCode = docUniqueCode;
        this.docNum = docNum;
        this.docName = docName;
        this.docDescription = docDescription;
        this.file = file;
    }
}
