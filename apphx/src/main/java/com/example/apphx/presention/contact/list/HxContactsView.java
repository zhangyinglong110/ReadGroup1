package com.example.apphx.presention.contact.list;

import com.example.apphx.basemvp.MvpView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */

public interface HxContactsView extends MvpView {

    /**
     * 设置联系人
     *
     * @param contacts
     */
    void setContacts(List<String> contacts);


    /**
     * 刷新联系人列表
     */
    void refreshContacts();

    /**
     * 删除联系人
     *
     * @param hxId
     */
    void deleteContact(String hxId);


    /**
     * 设置空对象
     */
    HxContactsView NULL = new HxContactsView() {
        @Override
        public void setContacts(List<String> contacts) {

        }

        @Override
        public void refreshContacts() {

        }

        @Override
        public void deleteContact(String msg) {

        }
    };
}
