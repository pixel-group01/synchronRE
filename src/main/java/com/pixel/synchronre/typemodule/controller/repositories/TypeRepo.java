package com.pixel.synchronre.typemodule.controller.repositories;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TypeRepo extends JpaRepository<Type, Long>
{
    @Query("select t from Type t where t.status = ?1")
    List<Type> findByStatus(PersStatus status);

    @Query("select t from Type t where t.status = 'ACTIVE'")
    List<Type> findActiveTypes();

    @Query("select t from Type t where t.typeGroup = ?1 and t.status = ?2")
    List<Type> findByTypeGroupAndStatus(TypeGroup typeGroup, PersStatus status);

    @Query("select t.typeGroup from Type t where t.typeId = ?1")
    TypeGroup findTypeGroupBTypeId(Long typeId);


    @Query("select t from Type t where (upper(t.name) like upper(concat('%', coalesce(?1, '') , '%')) or upper(t.uniqueCode) like upper(concat('%', coalesce(?1, ''), '%')) or t.typeGroup = ?2) and t.status = ?3")
    Page<Type> searchPageOfTypes(String key, TypeGroup typeGroup, PersStatus status, Pageable pageable);

    @Query("select t from Type t where (upper(t.name) like upper(concat('%', coalesce(?1, ''), '%')) or upper(t.uniqueCode) like upper(concat('%', coalesce(?1, ''), '%')) or t.typeGroup in ?2) and t.status = ?3")
    Page<Type> searchPageOfTypes(String key, Collection<TypeGroup> typeGroups, PersStatus status, Pageable pageable);

    @Query("select t from Type t where (upper(t.name) like upper(concat('%', coalesce(?1, ''), '%')) or upper(t.uniqueCode) like upper(concat('%', coalesce(?1, ''), '%'))) and t.status = ?2")
    Page<Type> searchPageOfTypes(String key, PersStatus status, Pageable pageable);

    @Query("select t from Type t where t.status = ?1")
    Page<Type> searchPageOfTypes(PersStatus status, Pageable pageable);

    //Long typeId, String typeGroup, String uniqueCode, String name, String status

    @Query("select new com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO(t.typeId, t.typeGroup, t.uniqueCode, t.name, t.status, t.objectFolder, t.typeOrdre) from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE' order by t.typeOrdre asc")
    List<ReadTypeDTO> findByTypeGroup(TypeGroup typeGroup);

    @Query("select t.typeId from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE' order by t.name")
    List<Long> findTypeIdsByTypeGroup(TypeGroup typeGroup);

    @Query("select t.typeId from Type t where upper(t.typeGroup) = upper(?1) and t.status = 'ACTIVE'")
    List<Long> getTypeIdsByTypeGroup(String typeGroup);

    @Query("select new com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO(t.typeId, t.typeGroup, t.uniqueCode, t.name, t.status) from Type t order by t.typeGroup, t.uniqueCode, t.typeId, t.objectFolder")
    List<ReadTypeDTO> findAllTypes();

    @Query("select new com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO(s.child.typeId, s.child.typeGroup, s.child.uniqueCode, s.child.name, s.child.status) from TypeParam s where s.parent.typeId = ?1 order by s.child.name")
    List<ReadTypeDTO> findSousTypeOf(Long parentId);

    @Query("select new com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO(s.child.typeId, s.child.typeGroup, s.child.uniqueCode, s.child.name, s.child.status) from TypeParam s where upper(s.parent.uniqueCode) = upper(?1)")
    List<ReadTypeDTO> findSousTypeOf(String parentUniqueCode);

    @Query("select tp.child from TypeParam  tp where tp.parent.typeId = ?1 and tp.status = 'ACTIVE' and tp.child.status ='ACTIVE'")
    List<Type> findActiveSousTypes(Long parentId);

    @Query("select tp.child.typeId from TypeParam  tp where tp.parent.typeId = ?1 and tp.child.status = 'ACTIVE' and tp.status = 'ACTIVE'")
    List<Long> findChildrenIds(Long parentId);

    @Query("select (count (stp)>0) from TypeParam stp where stp.child.typeId=?2 and stp.parent.typeId=?1")
    boolean isSousTypeOf(long parentId, long childId);

    @Query("select (count(t) > 0) from Type t where upper(t.uniqueCode) = upper(?1)")
    boolean existsByUniqueCode(String uniqueCode);

    @Query("select (count(t) > 0) from Type t where t.typeId <> :typeId and upper(t.uniqueCode) = upper(:uniqueCode)")
    boolean existsByUniqueCode(@Param("typeId") Long typeId, @Param("uniqueCode") String uniqueCode);

    @Query("select (count(stp) = 0) from TypeParam stp where stp.child.typeId = ?1 or stp.parent.typeId = ?1")
    boolean isDeletable(long typeId);

    @Query("select (count(stp) = 0) from TypeParam stp where stp.child.typeId = ?1 or stp.parent.typeId = ?1")
    boolean isDeletable(String uniqueCode);

    @Modifying
    @Query("delete from Type t where t.uniqueCode = ?1")
    long deleteByUniqueCode(String uniqueCode);

    @Modifying
    @Query("update Type t set t.typeGroup = :typeGroup, t.uniqueCode = :uniqueCode, t.name = :name where t.typeId =:typeId")
    long updateType(@Param("typeId") long typeId, @Param("typeGroup") String typeGroup, @Param("uniqueCode") String uniqueCode, @Param("name") String name);

    @Query("select t.uniqueCode from Type t where t.typeId = ?1")
    String getUniqueCode(Long typeId);

    @Query("select (count(t)>0) from Type t where t.typeGroup = ?1 and t.typeId = ?2 and t.status = 'ACTIVE'")
    boolean typeGroupHasChild(TypeGroup typeGroup, Long typeId);

    @Query("select (count(t)>0) from Type t where t.typeGroup = ?1 and t.uniqueCode = ?2 and t.status = 'ACTIVE'")
    boolean typeGroupHasChild(TypeGroup typeGroup, String uniqueCode);

    Optional<Type> findByUniqueCode(String uniqueCode);

    @Query("select t.typeId from Type t where t.uniqueCode = ?1")
    Long findTypeIdByUniqueCode(String uniqueCode);

    @Query("select t.objectFolder from Type t where t.uniqueCode = ?1")
    String getObjectFolderByUniqueCode(String uniqueCode);

    @Query("select t.uniqueCode from Type t where t.objectFolder = ?1")
    List<String> findUniqueCodesByObjectFolder(String objectFolder);

    @Query("select (count(t.typeParamId)>0) from TypeParam t where t.child.typeId = ?1")
    boolean typeHasAnyParent(Long typeId);

    @Query("select (count(t.typeParamId)>0) from TypeParam t where t.child.typeId = ?1 and t.child.typeGroup = ?2")
    boolean typeHasAnyParent(Long typeId, TypeGroup typeGroup);

    @Query("select t from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE' and (select count(tp.typeParamId) from TypeParam tp where tp.child.typeId = t.typeId and tp.status = 'ACTIVE') = 0")
    List<Type> findBaseTypes(TypeGroup typeGroup);

    @Query("select tp.child from TypeParam tp where tp.parent.typeId = ?1 and tp.status = 'ACTIVE'")
    List<Type> getChildren(Long typeId);

    @Query("select t.typeId from Type t where t.uniqueCode = ?1")
    Long getTypeIdbyUniqueCode(String uniqueCode);
}
