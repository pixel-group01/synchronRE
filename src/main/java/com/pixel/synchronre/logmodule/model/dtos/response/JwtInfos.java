package com.pixel.synchronre.logmodule.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class JwtInfos
{
    private Long userId;
    private String userEmail;
    private String nomPrenom;
    private List<String> authorities;
    private Long fncId;
    private String fncName;
    private Long cedId;
    private String cedName;
    private String cedSigle;
    private LocalDate fncStartingDate;
    private LocalDate fncEndingDate;
    private Date tokenStartingDate;
    private Date tokenEndingDate;
    private String connectionId;
}
