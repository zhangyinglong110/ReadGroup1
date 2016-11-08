package com.example.apphx.model.event;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 搜索联系人事件
 * Created by Administrator on 2016/11/8 0008.
 */

public class HxSearchContactEvent {
    public final List<EaseUser> contacts;
    public final boolean isSuccess;
    public final String errorMeassage;

    public HxSearchContactEvent(List<EaseUser> contacts) {
        this.contacts = contacts;
        isSuccess = true;
        errorMeassage = null;
    }

    public HxSearchContactEvent(String errorMeassage) {
        this.contacts = null;
        isSuccess = false;
        this.errorMeassage = errorMeassage;
    }

}
