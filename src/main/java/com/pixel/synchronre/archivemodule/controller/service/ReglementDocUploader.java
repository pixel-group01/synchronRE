package com.pixel.synchronre.archivemodule.controller.service;

import com.pixel.synchronre.archivemodule.controller.repositories.DocumentRepository;
import com.pixel.synchronre.archivemodule.model.dtos.DocMapper;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ReglementDocUploader extends AbstractDocumentUploader
{
	public ReglementDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo) {
		super(typeRepo, docMapper, docRepo);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToReglementDoc(dto);
	}
}