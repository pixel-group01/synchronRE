package com.pixel.synchronre.archivemodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Document
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOC_ID_GEN")
	@SequenceGenerator(name = "DOC_ID_GEN", sequenceName = "DOC_ID_GEN")
	private Long docId;
	private String docNum;
	private String docDescription;
	private String docPath;

	@ManyToOne @JoinColumn(name = "TYPE_ID")
	private Type docType;
	@ManyToOne @JoinColumn(name = "USER_ID")
	private AppUser user;
	@ManyToOne @JoinColumn(name = "AFF_ID")
	private Affaire affaire;
	@ManyToOne @JoinColumn(name = "REP_ID")
	private Repartition repartition;
	@ManyToOne @JoinColumn(name = "REG_ID")
	private Reglement reglement;
	@ManyToOne @JoinColumn(name = "SIN_ID")
	private Sinistre sinistre;

	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	@ManyToOne @JoinColumn(name = "STA_CODE")
	private Statut status;

	@Transient
	private MultipartFile file;
}
