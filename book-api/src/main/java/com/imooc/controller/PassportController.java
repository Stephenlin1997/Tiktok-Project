package com.imooc.controller;
import java.util.*;
import com.imooc.bo.RegistLoginBo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.IPUtil;
import com.imooc.utils.MyInfo;
import com.imooc.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "PassportController 通行证的接口模块")
@RequestMapping("passport")
@Slf4j
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtils smsUtils;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile,
                                      HttpServletRequest request) throws Exception{
        if(StringUtils.isBlank(mobile)){
            return GraceJSONResult.ok();
        }
        // 获得用户Ip
        String userIP = IPUtil.getRequestIp(request);
        // 根据用户IP进行限制,限制用户在60s之内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIP, userIP);

        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, code);

        //smsUtils.sendSMS(mobile, code);
        log.info(code);
        log.info(userIP);
        //TODO 把验证码放入redis中 用于后续的验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);

        return GraceJSONResult.ok();
    }

    @PostMapping("login")
    public GraceJSONResult login(@Valid @RequestBody RegistLoginBo registLoginBO,
                                 BindingResult result, HttpServletRequest request) throws Exception{
        //0. 判断bindingresult中是否保存了错误的验证信息，如果有，则需要返回到前端
        if(result.hasErrors()){
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }
        return GraceJSONResult.ok();
    }


}
