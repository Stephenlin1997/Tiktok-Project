package com.imooc.service;

import com.imooc.bo.UpdatedUserBO;
import com.imooc.bo.VlogBO;
import com.imooc.pojo.Users;

public interface VlogService {


    /**
     * 新增Vlog视频
     */
    public void createVlog(VlogBO vlogBO);

}
