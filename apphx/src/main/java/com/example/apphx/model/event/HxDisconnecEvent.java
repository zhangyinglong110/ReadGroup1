package com.example.apphx.model.event;

/**
 * 环信连接断开事件
 * Created by Administrator on 2016/11/5 0005.
 */

public class HxDisconnecEvent {

    public final int errorCode;

    public HxDisconnecEvent(int errorCode) {
        this.errorCode = errorCode;
    }
}
