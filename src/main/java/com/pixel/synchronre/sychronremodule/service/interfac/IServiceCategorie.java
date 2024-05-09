package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.net.UnknownHostException;

public interface IServiceCategorie{
    CategorieResp create(CategorieReq dto) throws UnknownHostException;
    CategorieResp update(CategorieReq dto);
    Page<CategorieResp> search(Long traiId, String key, PageRequest of);
}
