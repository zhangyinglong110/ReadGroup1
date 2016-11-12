package com.example.apphx.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.apphx.model.entity.InviteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认本地邀请信息仓库
 * Created by Administrator on 2016/11/9 0009.
 */

public class DefaultLocalInviteRepo implements ILocalInviteRepo {

    private String currentUserId;
    private final Context context;
    private final Gson gson;
    private SharedPreferences preferences;
    private static final String PREF_HX_INVITE_MESSAGE_NAME_ = "PREF_HX_INVITE_MESSAGE_NAME_";
    private static DefaultLocalInviteRepo sInstace;

    /**
     * 单例模式的应用
     *
     * @param context
     * @return
     */
    public static DefaultLocalInviteRepo getsInstace(Context context) {
        if (sInstace == null) {
            sInstace = new DefaultLocalInviteRepo(context.getApplicationContext());
        }
        return sInstace;
    }

    private DefaultLocalInviteRepo(Context context) {
        this.context = context;
        gson = new Gson();
    }

    @Override
    public void setCurrentUser(String hxId) {
        this.currentUserId = hxId;

        if (hxId == null) {
            this.preferences = null;
            return;
        }

        this.preferences = context.getSharedPreferences(PREF_HX_INVITE_MESSAGE_NAME_ + hxId, Context.MODE_PRIVATE);
    }

    @Override
    public void save(InviteMessage message) {
        if (currentUserId == null || !currentUserId.equals(message.getToHxId())) {
            throw new RuntimeException("Wrong current user: " + currentUserId);
        }

        preferences.edit()
                .putString(message.getFormId(), gson.toJson(message))
                .commit();
    }

    @Override
    public void delete(String fromHxId) {

        if (currentUserId == null) {
            throw new RuntimeException("No current user!");
        }

        preferences.edit()
                .remove(fromHxId)
                .commit();

    }

    @NonNull
    @Override
    public List<InviteMessage> getAll() {
        if (currentUserId == null) {
            throw new RuntimeException("No current user!");
        }

        Map<String, ?> map = preferences.getAll();

        ArrayList<InviteMessage> messages = new ArrayList<>();

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            InviteMessage inviteMessage = gson.fromJson(entry.getValue().toString(), InviteMessage.class);
            messages.add(inviteMessage);
        }
        return messages;
    }
}

