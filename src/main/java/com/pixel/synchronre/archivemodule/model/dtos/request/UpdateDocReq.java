package com.pixel.synchronre.archivemodule.model.dtos.request;

import com.pixel.synchronre.archivemodule.model.dtos.validator.ExistingDocId;
import com.pixel.synchronre.archivemodule.model.dtos.validator.ValidFileExtension;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class UpdateDocReq
{
    @ExistingDocId
    private Long docId;
    private String docUniqueCode;
    private String docNum;
    private String docDescription;
    private MultipartFile file;
    private String base64UrlFile;
    @ValidFileExtension
    private String extension;

    public UpdateDocReq(Long docId, String docUniqueCode, String docNum, String docDescription, MultipartFile file) {
        this.docId = docId;
        this.docUniqueCode = docUniqueCode;
        this.docNum = docNum;
        this.docDescription = docDescription;
        this.file = file;
    }
}
