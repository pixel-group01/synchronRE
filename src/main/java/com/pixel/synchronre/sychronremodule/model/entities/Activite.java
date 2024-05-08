package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

    @AllArgsConstructor @NoArgsConstructor @Getter @Setter
    @Entity
    public class Activite
    {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACT_ID_GEN")
        @SequenceGenerator(name = "ACT_ID_GEN", sequenceName = "ACT_ID_GEN")
        private Long activiteId;
        private String activiteLibelle;
        @ManyToOne
        @JoinColumn(name = "STA_CODE")
        protected Statut statut;
        @ManyToOne @JoinColumn(name = "user_creator")
        protected AppUser userCreator;
        @ManyToOne @JoinColumn(name = "fon_creator")
        protected AppFunction fonCreator;
        @CreationTimestamp
        protected LocalDateTime createdAt;
        @UpdateTimestamp
        protected LocalDateTime updatedAt;

        public Activite(Long activiteId) {
            this.activiteId = activiteId;
        }
    }


