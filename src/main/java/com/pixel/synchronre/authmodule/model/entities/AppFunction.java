package com.pixel.synchronre.authmodule.model.entities;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class AppFunction
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FNC_ID_GEN")
    @SequenceGenerator(name = "FNC_ID_GEN", sequenceName = "FNC_ID_GEN", allocationSize = 10)
    protected Long id;
    private Long visibilityId;
    private String name;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;

    public AppFunction(Long fncId) {
        this.id = fncId;
    }
}
