package com.example.apphx.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 提供默认的本地仓库的数据通过{@link android.content.SharedPreferences }来实现，
 * </p>
 * 如果满足不了需求，可以在App模块使用数据库来完成
 * Created by Administrator on 2016/11/8 0008.
 */

public class DefaultLocalUserRepo implements ILocalUsersRepo {
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private static DefaultLocalUserRepo sInsance;
    private static final String PREF_HX_USER_FILE_NAME = "PREF_HX_USER_FILE_NAME";

    public static DefaultLocalUserRepo getInstance(Context context) {
        if (sInsance == null) {
            sInsance = new DefaultLocalUserRepo(context.getApplicationContext());
        }
        return sInsance;
    }

    private DefaultLocalUserRepo(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_HX_USER_FILE_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }


    @Override
    public void saveAll(@NonNull List<EaseUser> userList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (EaseUser easeuser : userList) {
            editor.putString(easeuser.getUsername(), gson.toJson(easeuser));
        }
        editor.apply();
    }

    @Override
    public void save(@NonNull EaseUser easeUser) {
        //当用户信息有更新时重复往里存，使用的都是环信ID为key的，是会覆盖掉的
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(easeUser.getUsername(), gson.toJson(easeUser));
        editor.apply();
    }

    @Nullable
    @Override
    public EaseUser getUser(String hxId) {
        String userJsonStr = sharedPreferences.getString(hxId, null);
        if (userJsonStr == null) return null;
        return gson.fromJson(userJsonStr, EaseUser.class);
    }
}
