package com.example.apphx.model.repository;

import android.support.annotation.NonNull;

import com.example.apphx.model.entity.InviteMessage;

import java.util.List;

/**
 * 本地邀请信息的一个仓库接口
 * </p>
 * 本模块内将使用{@link android.content.SharedPreferences}来实现一个默认的本地仓库
 * Created by Administrator on 2016/11/9 0009.
 */

public interface ILocalInviteRepo {


    void setCurrentUser(String hxId);

    /**
     * 保存邀请信息
     *
     * @param message
     */
    void save(InviteMessage message);

    /**
     * 删除来自另一个好友的邀请
     *
     * @param fromHxId
     */
    void delete(String fromHxId);


    /**
     * 当前用户的所有信息
     *
     * @return
     */
    @NonNull
    List<InviteMessage> getAll();
}
