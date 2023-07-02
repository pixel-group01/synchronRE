package com.pixel.synchronre.logmodule.model.dtos.response;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConnexionList {
    private Long userId;
    private String userEmail;
    private String action;
    private LocalDateTime actionDateTime;
    private String ipAddress;
    private byte[] macAddress;
    private String hostName;
    private String connectionId;
    private AppFunction function;
}
