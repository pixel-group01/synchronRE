package com.pixel.synchronre.sychronremodule.model.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VTrancheCategorieRepo extends JpaRepository<VTrancheCategorie, Long>
{
  @Query("select tc.categorieId from  VTrancheCategorie tc where tc.trancheId = ?1")
  List<Long> getCatIdsByTrancheId(Long trancheId);
}