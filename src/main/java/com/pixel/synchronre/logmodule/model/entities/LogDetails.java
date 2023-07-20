package com.pixel.synchronre.logmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class LogDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objectId;
    @Column(length = 100000)
    private String newValue;
    @Column(length = 100000)
    private String oldValue;
    private String columnName;
    private String connectionId;
    private String tableName;
    @ManyToOne @JoinColumn(name = "log_Id")
    private Log log;
}
