package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<AppUser, Long>
{
    @Query("select (count(a) > 0) from AppUser a where upper(a.email) = upper(?1)")
    boolean alreadyExistsByEmail(String email);

    @Query("select (count(a) > 0) from AppUser a where a.email = ?1 and a.userId <> ?2")
    boolean alreadyExistsByEmail(String email, Long userId);

    @Query("select (count(a) > 0) from AppUser a where upper(a.tel) = upper(?1)")
    boolean alreadyExistsByTel(String tel);

    @Query("select (count(a) > 0) from AppUser a where upper(a.tel) = upper(?1) and a.userId <> ?2")
    boolean alreadyExistsByTel(String tel, Long userId);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByTel(String tel);

    @Query("select u.userId from AppUser u where u.email = ?1")
    Long getUserIdByEmail(String userEmail);

    @Query("select (count(u.userId)>0) from AppUser u where u.userId = ?1 and u.email = ?2")
    boolean existsByUserIdAndEmail(Long userId, String email);

    @Query("select (count(u.userId)>0) from AppUser u where u.email = ?1")
    boolean existsByEmail(String email);
}
