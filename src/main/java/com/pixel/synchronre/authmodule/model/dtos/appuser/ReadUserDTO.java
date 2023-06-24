package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.model.dtos.appfunction.ReadFncDTO;
import com.pixel.synchronre.authmodule.model.enums.UserStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadUserDTO
{
    private Long userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private UserStatus status;
    private ReadFncDTO currentFnc;

    public ReadUserDTO(Long userId, String firstName, String lastName, String password, String email, String tel, boolean active, boolean notBlocked) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.active = active;
        this.notBlocked = notBlocked;
    }
}
