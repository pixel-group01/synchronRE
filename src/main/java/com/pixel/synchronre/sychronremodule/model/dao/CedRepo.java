package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Cedente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CedRepo extends JpaRepository<Cedente, Long>
{
    @Query("select (count(c) > 0) from Cedente c where upper(c.cedEmail) = upper(?1)")
    boolean alreadyExistsByEmail(String cesEmail);

    @Query("select (count(c) > 0) from Cedente c where c.cedEmail = ?1 and c.cedId <> ?2")
    boolean alreadyExistsByEmail(String cesEmail, Long cesId);

    @Query("select (count(c) > 0) from Cedente c where upper(c.cedTel) = upper(?1)")
    boolean alreadyExistsByTel(String tel);

    @Query("select (count(c) > 0) from Cedente c where upper(c.cedTel) = upper(?1) and c.cedId <> ?2")
    boolean alreadyExistsByTel(String tel, Long cesId);
}
