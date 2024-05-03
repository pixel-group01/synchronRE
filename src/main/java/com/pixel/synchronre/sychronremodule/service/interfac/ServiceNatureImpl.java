package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dao.NatureRepository;
import com.pixel.synchronre.sychronremodule.model.dto.nature.response.NatureListResp;
import com.pixel.synchronre.sychronremodule.model.enums.FORME;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceNatureImpl implements IserviceNature
{
    private final NatureRepository natureRepo;
    @Override
    public List<NatureListResp> getListeNatures(String forme)
    {
        FORME formeEnum = EnumUtils.getEnum(FORME.class, forme);
        List<NatureListResp> natureListResps = natureRepo.findByForme(formeEnum);
        return natureListResps;
    }
}