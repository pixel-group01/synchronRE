package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.dtos.appuser.ListUserDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.ReadUserDTO;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("select f.user from AppFunction f where f.user.userId = ?1")
    AppUser findByFunctionId(Long fncId);

    @Query("""
        select new com.pixel.synchronre.authmodule.model.dtos.appuser.ListUserDTO(
        a.userId, a.firstName, a.lastName, ced.cedNomFiliale, ced.cedSigleFiliale, ces.cesNom, ces.cesSigle,
        a.email, a.tel, a.active, a.notBlocked) 
        from AppUser a left join Cedante ced on a.visibilityId = ced.cedId left join Cessionnaire ces
        where (a.cesId is null or a.cesId = ces.cesId) and (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(a.firstName, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(a.lastName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(ced.cedNomFiliale, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(ced.cedSigleFiliale, '') ) as string))) >0
        
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(ces.cesNom, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(ces.cesSigle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(a.email, '') ) as string))) >0
        
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(a.tel, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(a.email, '') ) as string))) >0
       
        )       
        and (:cedId is null or :cedId = ced.cedId) 
        and (:cesId is null or :cesId = ces.cesId) 
        and (a.statut.staCode is null or a.statut.staCode in :staCodes)
""")
    Page<ListUserDTO> searchUsers(@Param("key") String key,
                                  @Param("cedId") Long cedId,
                                  @Param("cesId") Long cesId,
                                  @Param("staCodes") List<String> staCode,
                                  Pageable pageable);

    @Query("""
        select new com.pixel.synchronre.authmodule.model.dtos.appuser.ReadUserDTO(
            u.userId, u.firstName, u.lastName, '', u.email, u.tel, u.active, u.notBlocked
        )
        from AppUser u where u.userId = ?1
    """)
    ReadUserDTO findReadUserDto(Long userId);

    @Query("select u.cesId from AppUser u where u.userId = ?1")
    Long getUserCesId(Long userId);

    @Query("select u.visibilityId from AppUser u where u.userId = ?1")
    Long getVisibilityId(Long userId);
}
