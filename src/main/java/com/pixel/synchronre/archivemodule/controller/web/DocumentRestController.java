package com.pixel.synchronre.archivemodule.controller.web;

import com.pixel.synchronre.archivemodule.controller.repositories.DocumentRepository;
import com.pixel.synchronre.archivemodule.controller.service.AbstractDocumentService;
import com.pixel.synchronre.archivemodule.controller.service.DocServiceProvider;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.archivemodule.model.dtos.response.ReadDocDTO;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.Base64ToFileConverter;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/documents") @ResponseStatus(HttpStatus.OK)
public class DocumentRestController
{
    private final DocumentRepository docRepo;
    private final TypeRepo typeRepo;
    private final DocServiceProvider docServiceProvider;
    private final AbstractDocumentService docService;

    @GetMapping(path = "/{typeDocUniqueCode}/types")
    public List<ReadTypeDTO> getTypeDocumentReglement(@PathVariable String typeDocUniqueCode) throws UnknownHostException {
        Type typeDoc = typeDocUniqueCode == null ? null : typeRepo.findByUniqueCode(typeDocUniqueCode.toUpperCase());
        if(typeDoc == null) return new ArrayList<>();
        if(typeDoc.getTypeGroup() != TypeGroup.DOCUMENT) return new ArrayList<>();

        return typeRepo.findSousTypeOf(typeDoc.getTypeId());
    }

    @PostMapping(path = "/{groupDocUniqueCode}/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean uploadDocument(@RequestParam MultipartFile file, @RequestParam Long objectId, @RequestParam String docNum, @RequestParam String docDescription, @RequestParam String typeDocUniqueCode, @PathVariable String groupDocUniqueCode) throws IOException {
        AbstractDocumentService docUploader = docServiceProvider.getDocUploader(groupDocUniqueCode);
        String base64FileString = Base64ToFileConverter.convertToBase64UrlString(file);
        if(docUploader == null)  throw new AppException("Ce type de document n'est pas pris en charge par le système");
        UploadDocReq dto = new UploadDocReq(objectId, typeDocUniqueCode.toUpperCase(Locale.ROOT), docNum, docDescription, file);
        docUploader.uploadDocument(dto);
        return true;
    }

    @PostMapping(path = "/{groupDocUniqueCode}/upload")
    public boolean uploadDocument(@RequestBody UploadDocReq dto, @PathVariable String groupDocUniqueCode) throws UnknownHostException
    {
        AbstractDocumentService docUploader = docServiceProvider.getDocUploader(groupDocUniqueCode);
        if(docUploader == null)  throw new AppException("Ce type de document n'est pas pris en charge par le système");
        dto.setFile(Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), "." + dto.getExtension()));
        docUploader.uploadDocument(dto);
        return true;
    }

    @GetMapping(path = "/affaire/{affId}")
    public List<ReadDocDTO> getAffDocs(@PathVariable Long affId)
    {
        return docRepo.getAllDocsForObject(affId, null, null, null, null);
    }

    @GetMapping(path = "/placement/{plaId}")
    public List<ReadDocDTO> getPlaDocs(@PathVariable Long plaId)
    {
        return docRepo.getAllDocsForObject(null, plaId, null, null, null);
    }

    @GetMapping(path = "/reglement/{regId}")
    public List<ReadDocDTO> getRegDocs(@PathVariable Long regId)
    {
        return docRepo.getAllDocsForObject(null, null, regId, null, null);
    }

    @GetMapping(path = "/sinistre/{sinId}")
    public List<ReadDocDTO> getSinDocs(@PathVariable Long sinId)
    {
        return docRepo.getAllDocsForObject(null, null, null, sinId, null);
    }

    @GetMapping(path = "/user/{userId}")
    public List<ReadDocDTO> getUserDocs(@PathVariable Long userId)
    {
        return docRepo.getAllDocsForObject(null, null, null, null, userId);
    }

    @GetMapping(path = "/get-base64-url/{docId}")
    public Base64FileDto displayDocument(@PathVariable Long docId) throws Exception
    {
        Document doc = docRepo.findById(docId).orElse(null);
        if(doc == null) return null;
        String docPath = doc.getDocPath();
        byte[] docBytes = docService.downloadFile(docPath);
        String base64UrlString = Base64ToFileConverter.convertBytesToBase64UrlString(docBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64UrlString);
    }


}
