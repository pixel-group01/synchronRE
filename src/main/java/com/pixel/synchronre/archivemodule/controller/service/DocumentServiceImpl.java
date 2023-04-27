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
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component @RequiredArgsConstructor
public class DocumentServiceImpl implements IServiceDocument
{
	private final TypeRepo typeRepo;
	private final DocMapper docMapper;
	private final DocumentRepository docRepo;
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
	public String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName)
	{
		if(!typeRepo.existsByUniqueCode(typeCode)) return "";
		String uuid = UUID.randomUUID().toString().substring(0, 9);

		return DocumentsConstants.UPLOADS_DIR + "\\" + objectFolder+ "\\" +typeCode + "\\"+
				StringUtils.stripAccents(objectName).replace(" ", "_") + uuid + "." + FilenameUtils.getExtension(file.getOriginalFilename());
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
	public void uploadRecuReglement(UploadDocReq dto)
	{
		Type docType = typeRepo.findByUniqueCode(dto.getDocUniqueCode());
		Document recuPaie = docMapper.mapToRecuPaiementDoc(dto);
		String path = this.generatePath(dto.getFile(), docType.getObjectFolder(), dto.getDocUniqueCode(), recuPaie.getDocDescription());
		recuPaie.setDocPath(path);
		docRepo.save(recuPaie);
	}

	@Override
	public void uploadPhoto(UploadDocReq dto)
	{
		Type docType = typeRepo.findByUniqueCode(dto.getDocUniqueCode());
		Document photo = docMapper.mapToPhotoDoc(dto);
		String path = this.generatePath(dto.getFile(), docType.getObjectFolder(), dto.getDocUniqueCode(), photo.getDocDescription());
		photo.setDocPath(path);
		docRepo.save(photo);
	}

	@Bean @Order(2)
	CommandLineRunner createSystemDirectories(TypeRepo typeRepo)
	{
		return(args)->
		{
			File agtUploadDir= new File(DocumentsConstants.UPLOADS_DIR);
			if(!agtUploadDir.exists()) agtUploadDir.mkdirs();
			typeRepo.findByTypeGroup(TypeGroup.DOCUMENT).forEach(type->
			{
				File typeDir = new File(DocumentsConstants.UPLOADS_DIR + "\\" + type.getUniqueCode()) ;
				if(!typeDir.exists()) typeDir.mkdirs();
			});
		};
	}
}
