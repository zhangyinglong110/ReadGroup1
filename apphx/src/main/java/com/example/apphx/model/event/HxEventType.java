package com.example.apphx.model.event;

/**
 * 事件类型，用于{@link HxErrorEvent}和{@link HxSimpleEvent}
 */
public enum HxEventType {
    LOGIN, // 登录环信服务器
    REGISTER, // 注册环信服务器(只用于测试)
    DELETE_CONTACT, // 删除联系人
    SEND_INVITE, //发送好友的一个邀请
    ACCEPT_INVITE, //接受一个好友邀请
    REFUSE_INVITE //拒绝好友的邀请

}
