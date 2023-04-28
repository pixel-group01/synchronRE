package com.pixel.synchronre.archivemodule.controller.service;

import com.pixel.synchronre.archivemodule.controller.repositories.DocumentRepository;
import com.pixel.synchronre.archivemodule.model.constants.DocumentsConstants;
import com.pixel.synchronre.archivemodule.model.dtos.DocMapper;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component @RequiredArgsConstructor
public abstract class AbstractDocumentUploader
{
	protected final TypeRepo typeRepo;
	protected final DocMapper docMapper;
	protected final DocumentRepository docRepo;

	public byte[] downloadFile(String filePAth)
	{
		File file = new File(filePAth);
		Path path = Paths.get(file.toURI());
		try
		{
			return Files.readAllBytes(path);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new AppException("Erreur de téléchargement");
		}
	}

	public void uploadDocument(UploadDocReq dto) {
		Type docType = typeRepo.findByUniqueCode(dto.getDocUniqueCode());
		Document doc = mapToDocument(dto);
		String path = generatePath(dto.getFile(), docType.getObjectFolder(), dto.getDocUniqueCode(), doc.getDocDescription());
		doc.setDocPath(path);
		saveDocument(doc);
		uploadFile(dto.getFile(), doc.getDocPath());
	}

	protected abstract Document mapToDocument(UploadDocReq dto);


	private String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName)
	{
		if(!typeRepo.existsByUniqueCode(typeCode)) return "";
		String uuid = UUID.randomUUID().toString().substring(0, 9);

		return DocumentsConstants.UPLOADS_DIR + "\\" + objectFolder+ "\\" +typeCode + "\\"+
				StringUtils.stripAccents(objectName).replace(" ", "_") + uuid + "." + FilenameUtils.getExtension(file.getOriginalFilename());
	}

	private void saveDocument(Document doc) {
		docRepo.save(doc);
	}

	private void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException
	{
		try
		{
			Files.write(Paths.get(destinationPath), file.getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}