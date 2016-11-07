package com.example.apphx.model.event;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * 当有新的消息的时候，发送的EventBus的事件
 * Created by Administrator on 2016/11/7 0007.
 */

public class HxNewMsgEvent {
    public final List<EMMessage> list;

    public HxNewMsgEvent(List<EMMessage> list) {
        this.list = list;
    }
}
