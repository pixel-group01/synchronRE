package com.pixel.synchronre.archivemodule.controller.service;

import com.pixel.synchronre.archivemodule.controller.repositories.DocumentRepository;
import com.pixel.synchronre.archivemodule.model.dtos.DocMapper;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.springframework.stereotype.Component;

@Component("sinistre")
public class SinistreDocUploader extends AbstractDocumentService
{
	public SinistreDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo) {
		super(typeRepo, docMapper, docRepo);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToSinistreDoc(dto);
	}
}