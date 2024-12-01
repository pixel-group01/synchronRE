package com.pixel.synchronre.logmodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Inheritance(strategy = InheritanceType.JOINED)
public class Log
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_ID_GEN")
    @SequenceGenerator(name = "LOG_ID_GEN", sequenceName = "LOG_ID_GEN", allocationSize = 10)
    private Long id;
    private Long userId;
    private String userEmail;
    private String action;
    @CreationTimestamp
    private LocalDateTime actionDateTime;
    private String ipAddress;
    private byte[] macAddress;
    private String hostName;
    private String connectionId;
    @ManyToOne @JoinColumn(name = "FUNC_ID")
    private AppFunction function;
    @Column(length = 500000)
    private String errorMessage;
    @Column(length = 500000)
    private String stackTrace;
    @Transient
    private List<LogDetails> logDetails;

    @Override
    public String toString() {
        return id + "_" + userId + "_" + userEmail + "_" + action;
    }
}
