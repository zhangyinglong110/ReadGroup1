package com.example.apphx.model.repository;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * 假的远程仓库的数据
 * Created by Administrator on 2016/11/8 0008.
 */

public class MockRemoteUserRepo implements IRemoteUserRepo {
    @Override
    public List<EaseUser> queryByName(String username) throws Exception {

        //现在模拟数据，到自己应用的时候将提供一个搜索的接口请求数据，在APP模块
        Thread.sleep(3000);
        ArrayList<EaseUser> easeUsers = new ArrayList<>();
        easeUsers.add(new EaseUser("test01"));
        easeUsers.add(new EaseUser("test02"));
        easeUsers.add(new EaseUser("test03"));
        return easeUsers;
    }

    @Override
    public List<EaseUser> getUser(List<String> ids) throws Exception {
        return null;
    }
}
