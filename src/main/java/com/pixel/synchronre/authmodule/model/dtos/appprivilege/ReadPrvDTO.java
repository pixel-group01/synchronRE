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
}
