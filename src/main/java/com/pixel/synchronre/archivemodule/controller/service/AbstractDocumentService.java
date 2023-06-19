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
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component @RequiredArgsConstructor
public abstract class AbstractDocumentService implements IServiceDocument
{
	protected final TypeRepo typeRepo;
	protected final DocMapper docMapper;
	protected final DocumentRepository docRepo;

	@Override
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

	@Override
	public boolean deleteFile(String filePath)
	{
		File file = new File(filePath);
		return file == null ? false : file.delete();
	}

	protected abstract Document mapToDocument(UploadDocReq dto);


	@Override
	public String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName)
	{
		if(!typeRepo.existsByUniqueCode(typeCode)) return "";
		String uuid = UUID.randomUUID().toString().substring(0, 9);

		return DocumentsConstants.UPLOADS_DIR + "\\" + objectFolder+ "\\" +typeCode + "\\"+
				StringUtils.stripAccents(objectName).replace(" ", "_") + uuid + "." + FilenameUtils.getExtension(file.getOriginalFilename());
	}

	private void saveDocument(Document doc) {
		docRepo.save(doc);
	}

	@Override
	public void renameFile(String oldPath, String newPath)
	{
		if(new File(oldPath).exists())
		{
			try {
				Files.move(Paths.get(oldPath), Paths.get(newPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException
	{
		try
		{
			Files.write(Paths.get(destinationPath), file.getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
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


	@Override
	public void displayPdf(HttpServletResponse response, byte[] fileBytes, String displayName)  throws Exception
	{
		// Configurez l'en-tête de la réponse HTTP
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=" + displayName +".pdf");
		response.setContentLength(fileBytes.length);

		// Écrivez le rapport Jasper dans le flux de sortie de la réponse HTTP
		OutputStream outStream = response.getOutputStream();
		outStream.write(fileBytes);
		outStream.flush();
		outStream.close();
	}

	@Bean
	//@DependsOn("commandLineRunner")
	CommandLineRunner createSystemDirectories(TypeRepo typeRepo)
	{
		return(args)->
		{
			File agtUploadDir= new File(DocumentsConstants.UPLOADS_DIR);
			if(!agtUploadDir.exists()) agtUploadDir.mkdirs();
			typeRepo.findByTypeGroup(TypeGroup.DOCUMENT).forEach(type->
			{
				String objectFolder = type.getObjectFolder();
				typeRepo.findUniqueCodesByObjectFolder(objectFolder).forEach(uc->
				{
					File typeDir = new File(DocumentsConstants.UPLOADS_DIR  + "\\" + objectFolder + "\\" + type.getUniqueCode()) ;
					if(!typeDir.exists()) typeDir.mkdirs();
				});
			});
		};
	}
}