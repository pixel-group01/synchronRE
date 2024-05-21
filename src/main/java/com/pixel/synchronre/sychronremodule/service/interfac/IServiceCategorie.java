package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceCategorie{
    CategorieResp create(CategorieReq dto);
    CategorieResp update(CategorieReq dto);
    CategorieResp save(CategorieReq dto);
    boolean delete(Long catId);
    Page<CategorieResp> search(Long traiId, String key, Pageable pageable);

    List<CategorieResp> getCategorieList(Long traiteNpId);
}
