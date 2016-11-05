package com.example.apphx.model.event;

/**
 * 事件类型，用于{@link HxErrorEvent}和{@link HxSimpleEvent}
 */
public enum HxEventType {
    LOGIN, // 登录环信服务器
    REGISTER, // 注册环信服务器(只用于测试)
    DELETE_CONTACT, // 删除联系人

}
