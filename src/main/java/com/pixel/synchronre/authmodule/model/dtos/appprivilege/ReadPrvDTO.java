package com.pixel.synchronre.authmodule.model.dtos.appprivilege;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadPrvDTO
{
    private Long privilegeId;
    private String privilegeCode;
    private String privilegeName;
    private String prvTypeName;
    private Long clientId;
    private String clientName;

    public ReadPrvDTO(Long privilegeId, String privilegeCode, String privilegeName, String prvTypeName) {
        this.privilegeId = privilegeId;
        this.privilegeCode = privilegeCode;
        this.privilegeName = privilegeName;
        this.prvTypeName = prvTypeName;
    }
}
