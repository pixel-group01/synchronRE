package com.pixel.synchronre.archivemodule.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReadDocDTO
{
	private Long docId;
	private String docNum;
	private String docDescription;
	private String docPath;
	private Long docTypeId;
	private String docTypeUniqueCode;
	private String docTypeName;

}
