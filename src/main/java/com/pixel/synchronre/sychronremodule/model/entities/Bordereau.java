package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Bordereau {
    @Id
    private String bordNum;
    @ManyToOne @JoinColumn(name = "repId")
    private Repartition repartition;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}
