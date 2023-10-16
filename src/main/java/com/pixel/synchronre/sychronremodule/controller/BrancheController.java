package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.branche.request.CreateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.branche.request.UpdateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBranche;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BrancheController {

    private final IserviceBranche brancheService;


    @PostMapping(path = "/create")
    public BrancheDetailsResp createBranche(@RequestBody @Valid CreateBrancheReq dto) throws UnknownHostException {
        return brancheService.createBranche(dto);
    }

    @PutMapping(path = "/update")
    public BrancheDetailsResp updateBranche(@RequestBody @Valid UpdateBrancheReq dto) throws UnknownHostException {
        return brancheService.updateBranche(dto);
    }

    @GetMapping(path = "/list")
    public Page<BrancheListResp> searchBranches(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) throws UnknownHostException {
        return brancheService.searchBranche(key, PageRequest.of(page, size));
    }
}
