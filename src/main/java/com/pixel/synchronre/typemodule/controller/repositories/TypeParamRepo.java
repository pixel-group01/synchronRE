package com.pixel.synchronre.typemodule.controller.repositories;

import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypeParamRepo extends JpaRepository<TypeParam, Long>
{
    @Query("select s from TypeParam s where s.parent.typeId = ?1")
    List<TypeParam> findByParent(Long parentId);

    @Query("select s from TypeParam s where upper(s.parent.uniqueCode) = upper(?1)")
    List<TypeParam> findByParent(String parentUniqueCode);

    @Query("select t from TypeParam t where t.parent.typeId = ?1 and t.child.typeId = ?2")
    TypeParam findByParentAndChild(Long parentId, Long childId);

    @Query("select (count(s) > 0) from TypeParam s where s.parent.typeId = :parentId and s.child.typeId = :childId")
    boolean alreadyExists(@Param("parentId") Long parentId, @Param("childId") Long childId);

    @Query("select (count(t) > 0) from TypeParam t where t.parent.typeId = ?1 and t.child.typeId = ?2 and t.status = 'ACTIVE'")
    boolean alreadyExistsAndActive(Long parentId, Long childId);

    @Query("select (count(t) > 0) from TypeParam t where t.parent.typeId = ?1 and t.child.typeId = ?2 and t.status = 'DELETED'")
    boolean alreadyExistsAndNotActive(Long parentId, Long childId);

    @Query("select (count(tp)> 0 ) from TypeParam tp where tp.parent.typeId = ?1 and tp.child.typeId = ?2 and tp.status = 'ACTIVE'")
    boolean parentHasDirectSousType(Long parentId, Long childId);

    @Modifying
    @Query("delete from TypeParam s where s.parent.typeId = :parentId and s.child.typeId = :childId")
    int removeSousType(@Param("parentId") Long parentId, @Param("childId") Long childId);
}