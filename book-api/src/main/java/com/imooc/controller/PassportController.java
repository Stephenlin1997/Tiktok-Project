package com.imooc.controller;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.IPUtil;
import com.imooc.utils.MyInfo;
import com.imooc.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
}
