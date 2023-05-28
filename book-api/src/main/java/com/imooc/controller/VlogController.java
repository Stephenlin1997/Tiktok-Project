package com.imooc.controller;

import com.imooc.bo.VlogBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.model.Stu;
import com.imooc.service.VlogService;
import com.imooc.utils.SMSUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "VlogController 短视频相关业务功能的接口")
@RequestMapping("vlog")
@Slf4j
public class VlogController {
    @Autowired
    private VlogService vlogService;

    @PostMapping ("publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO) {
        vlogService.createVlog(vlogBO);

        return GraceJSONResult.ok();
    }
}