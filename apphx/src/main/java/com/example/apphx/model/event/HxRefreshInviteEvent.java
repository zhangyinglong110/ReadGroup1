package com.example.apphx.model.event;

import com.example.apphx.model.entity.InviteMessage;

import java.util.List;

/**
 * 环信刷新邀请界面的一个Event事件
 * Created by Administrator on 2016/11/10 0010.
 */

public class HxRefreshInviteEvent {

    public final List<InviteMessage> message;

    public HxRefreshInviteEvent(List<InviteMessage> message) {
        this.message = message;
    }
}
