package com.imooc.controller;
import java.util.*;
import com.imooc.bo.RegistLoginBo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.MyInfo;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private UserService userService;


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
//                                 BindingResult result, //对代码有侵入性
                                 HttpServletRequest request) throws Exception{
        //0. 判断bindingresult中是否保存了错误的验证信息，如果有，则需要返回到前端
//        if(result.hasErrors()){
//            Map<String, String> map = getErrors(result);
//            return GraceJSONResult.errorMap(map);
//        }
        registLoginBO.getMobile();

        String mobile = registLoginBO.getMobile();

        String code = registLoginBO.getSmsCode();

        //从redis中获得验证码 校验是否匹配
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);

        if(StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }


        //查询数据库 判断用户是否存在

        Users user = userService.queryMobileIsExist(mobile);
        if(user == null){
            //2.1 如果用户为空 表示没有注册过, 则为null, 需要创建信息入库
            user = userService.createUsers(mobile);

        }

        //3 如果不为空 可以保存信息以及用户会话信息
        //Redis timeout 时间可以不设置 即为永久保存 如果需要设置可以7天-30天
        String uToken = UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN + "" + user.getId(), uToken);


        //4 用户登陆注册成功以后 删除redis 里面的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        //5 返回用户信息 包含token令牌
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(uToken);


        return GraceJSONResult.ok(usersVO);

    }


    @PostMapping("logout")
    public GraceJSONResult login(@RequestParam String userId,
                                 HttpServletRequest request) throws Exception{
        //后端只需要清除用户token即可 前端也需要清除 清除本地app中的用户信息和token会话
        redis.del( REDIS_USER_TOKEN + ":" + userId);


        return GraceJSONResult.ok();

    }

}
