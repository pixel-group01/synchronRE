package com.pixel.synchronre.sychronremodule.model.dao;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BanqueRepository extends JpaRepository<Banque, Long> {

    @Query("select (count(b) > 0) from Banque b where upper(b.banCode) = upper(?1)")
    boolean alreadyExistsByCode(String banCode);

    @Query("select (count(b) > 0) from Banque b where b.banCode = ?1 and b.banId <> ?2")
    boolean alreadyExistsByCode(String banCode, Long banId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp(b.banId, b.banCode,b.banNumCompte,b.banIban,b.banCodeBic, b.banLibelle, b.banLibelleAbrege, 
        b.statut.staLibelle) 
        from Banque b where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(b.banCode, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(b.banNumCompte, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(b.banIban, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(b.banCodeBic, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(b.banLibelle, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(b.banLibelleAbrege, '') ) as string)) ) >0 ) 
                                         and b.statut.staCode = 'ACT'     
""")
    Page<BanqueListResp> searchBanques(String key, Pageable pageable);
}

