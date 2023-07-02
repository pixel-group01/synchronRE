package com.pixel.synchronre.authmodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Menu
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MNU_ID_GEN")
    @SequenceGenerator(name="MNU_ID_GEN", sequenceName = "MNU_ID_GEN", allocationSize=10)
    private Long menuId;
    @Column(unique = true)
    private String menuCode;
    @Column(unique = true)
    private String name;
    private String prvsCodesChain;
    @Transient
    private List<String> prvsCodes;
    @Enumerated(EnumType.STRING)
    private PersStatus status;
    @Transient
    public static final String chainSeparator = "::";

    public List<String> getPrvsCodes()
    {
        if(this.prvsCodesChain == null) return new ArrayList<>();
        return Arrays.asList(this.prvsCodesChain.split(Menu.chainSeparator));
    }

    public Menu(Long menuId, String menuCode, String name, String prvsCodesChain, PersStatus status) {
        this.menuId = menuId;
        this.menuCode = menuCode;
        this.name = name;
        this.prvsCodesChain = prvsCodesChain;
        this.status = status;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class MenuResp
    {
        private String menuCode;
        private String name;
    }
}
