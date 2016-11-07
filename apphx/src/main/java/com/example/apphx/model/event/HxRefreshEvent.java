package com.example.apphx.model.event;

import java.util.List;

/**
 * //获得联系人列表的数据时发生的Event事件
 * Created by Administrator on 2016/11/3 0003.
 */

public class HxRefreshEvent {

    public final List<String> contacts;

    //true 代表联系人 发生了变化
    public final boolean changed;


    public HxRefreshEvent(List<String> contacts) {
        this.contacts = contacts;
        changed = true;
    }

    public HxRefreshEvent() {
        this.changed = false;
        this.contacts = null;
    }


}
