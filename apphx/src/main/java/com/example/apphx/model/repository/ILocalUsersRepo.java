package com.example.apphx.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 本地用户信息的仓库
 * </p>
 * 本模块内将使用{@link android.content.SharedPreferences}来实现一个默认的本地仓库
 * Created by Administrator on 2016/11/8 0008.
 */

public interface ILocalUsersRepo {
    /**
     * 保存用户列表
     *
     * @param userList
     */
    void saveAll(@NonNull List<EaseUser> userList);

    /**
     * 保存用户
     *
     * @param easeUser
     */
    void save(@NonNull EaseUser easeUser);


    /**
     * 获取用户
     *
     * @param hxId
     * @return
     */
    @Nullable
    EaseUser getUser(String hxId);
}
