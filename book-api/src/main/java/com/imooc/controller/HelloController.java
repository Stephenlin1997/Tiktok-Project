package com.imooc.controller;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.model.Stu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloController {
    @GetMapping("hello")
    public Object hello(){

        Stu stu = new Stu("mike", 18);
        log.info(stu.toString());
        log.debug(stu.toString());
        log.error(stu.toString());
        log.warn(stu.toString());
        return GraceJSONResult.ok(stu);
        //return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_GLOBAL);
    }
}
