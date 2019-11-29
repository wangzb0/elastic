package com.elastic.controller;

import com.elastic.entity.ESPackageQueryDTO;
import com.elastic.entity.RestResponse;
import com.elastic.es.service.ESPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private ESPackageService esPackageService;


    @GetMapping("syncData")
    public RestResponse syncData() {
        esPackageService.syncMySqlToESByNewData(0L);
        return new RestResponse();
    }

    @GetMapping("getPackagePageByES")
    public RestResponse<ESPackageQueryDTO> doGetPackagePageByES(
            @RequestParam(name = "pageSize") Long pageSize,
            @RequestParam(name = "pageNum") Long pageNum,
            @RequestParam(name = "queryStr", required = false) String queryStr) {
        return RestResponse.of(esPackageService.doGetPackagePageByES(pageSize, pageNum, queryStr));
    }
}

