package com.example.apphx.model.repository;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 远程的一个仓库接口，代表到应用服务器获取数据的相关操作
 * </p>
 * 前期为了测试，本模块将提供一个假实现
 * Created by Administrator on 2016/11/8 0008.
 */

public interface IRemoteUserRepo {

    /**
     * 通过用户名查询用户
     */
    List<EaseUser> queryByName(String username) throws Exception;


    /**
     * 通过环信id查询用户信息
     */
    List<EaseUser> getUser(List<String> ids) throws Exception;
}
