package com.pixel.synchronre.authmodule.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueTel
public class UpdateUserDTO
{
    @ExistingUserId
    private Long userId;
    private String tel;
}
