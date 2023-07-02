package com.pixel.synchronre.logmodule.model.dtos.response;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConnexionList {
    private Long userId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String action;
    private LocalDateTime actionDateTime;
    private String ipAddress;
    private String hostName;
    private String connectionId;
    protected Long foncId;
    private String foncName;

    private String cedName;
    private String cedSigle;

    public ConnexionList(Long userId, String userEmail, String firstName, String lastName, String action, LocalDateTime actionDateTime, String ipAddress, String hostName, String connectionId, Long foncId, String foncName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.action = action;
        this.actionDateTime = actionDateTime;
        this.ipAddress = ipAddress;
        this.hostName = hostName;
        this.connectionId = connectionId;
        this.foncId = foncId;
        this.foncName = foncName;
    }
}
