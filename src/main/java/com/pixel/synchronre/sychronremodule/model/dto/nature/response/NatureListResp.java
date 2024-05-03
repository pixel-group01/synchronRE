package com.pixel.synchronre.sychronremodule.model.dto.nature.response;

import com.pixel.synchronre.sychronremodule.model.enums.FORME;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class NatureListResp {
    @Id
    private String natCode;
    private String natLibelle;
    private String formeCode;
    private String formeLibelle;
    private String staCode;
    private String staLibelle;

    public NatureListResp(String natCode, String natLibelle, FORME forme, String staCode, String staLibelle) {
        this.natCode = natCode;
        this.natLibelle = natLibelle;
        this.formeCode = forme.getFormeCode();
        this.formeLibelle = forme.getFormeLibelle();
        this.staCode = staCode;
        this.staLibelle = staLibelle;
    }
}
