package com.imooc.controller;

import com.imooc.MinIOConfig;
import com.imooc.bo.UpdatedUserBO;
import com.imooc.enums.FileTypeEnum;
import com.imooc.enums.UserInfoModifyType;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.Stu;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.MinIOUtils;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "userInfoController 用户信息接口模块")
@RequestMapping("userInfo")
@Slf4j
public class UserInfoController extends BaseInfoProperties{


    @Autowired
    private UserService userService;

    @GetMapping("query")
    public Object query(@RequestParam String userId) throws Exception{
        Users user = userService.getUsers(userId);

        UsersVO usersVO = new UsersVO();

        BeanUtils.copyProperties(user, usersVO);

        //我的关注总量
        String myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        //我的粉丝总数
        String myFansCountsStr = redis.get(REDIS_MY_FANS_COUNTS + ":" + userId);
        //获赞总数，视频+评论（点赞/喜欢）总和
        String likedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS+ ":" + userId);
        String likedVlogerCountsStr =  redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0, myFansCounts = 0, totalLikeMeCounts = 0,
                likedVlogCounts = 0, likedVlogerCounts = 0;

        if(StringUtils.isNotBlank(myFollowsCountsStr)){
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if(StringUtils.isNotBlank(myFansCountsStr)){
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }

        if(StringUtils.isNotBlank(likedVlogCountsStr)){
            likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
        }

        if(StringUtils.isNotBlank(likedVlogerCountsStr)){
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }

        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        usersVO.setMyFollowsCounts(myFollowsCounts);
        usersVO.setMyFansCounts(myFansCounts);
        usersVO.setTotalLikeMeCounts(totalLikeMeCounts);

        return GraceJSONResult.ok(usersVO);
    }

    @PostMapping ("modifyUserInfo")
    public GraceJSONResult modifyUserInfo(@RequestBody UpdatedUserBO updatedUserBO,
                                          @RequestParam Integer type)
            throws Exception{

        UserInfoModifyType.checkUserInfoTypeIsRight(type);

        Users newUserInfo = userService.updateUserInfo(updatedUserBO, type);

        return GraceJSONResult.ok(newUserInfo);
    }

    @Autowired
    private MinIOConfig minIOConfig;

    @PostMapping ("modifyImage")
    public GraceJSONResult modifyImage(@RequestParam String userId,
                                       @RequestParam Integer type,
                                       MultipartFile file) throws Exception{
        if(type != FileTypeEnum.BGIMG.type && type != FileTypeEnum.FACE.type){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String fileName = file.getOriginalFilename();

        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                fileName,
                file.getInputStream());

        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + fileName;

        //修改图片地址到数据库
        UpdatedUserBO updatedUserBO = new UpdatedUserBO();
        updatedUserBO.setId(userId);

        if(type == FileTypeEnum.BGIMG.type){
            updatedUserBO.setBgImg(imgUrl);
        }else{
            updatedUserBO.setFace(imgUrl);
        }
        updatedUserBO.setBgImg(imgUrl);

        Users users = userService.updateUserInfo(updatedUserBO);

        return GraceJSONResult.ok(users);
    }


}
