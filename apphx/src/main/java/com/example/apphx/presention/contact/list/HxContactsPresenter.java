package com.example.apphx.presention.contact.list;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxContactsManager;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxRefreshEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/11/4 0004.
 */

public class HxContactsPresenter extends MvpPresenter<HxContactsView> {
    @Override
    public HxContactsView getNullObject() {
        return HxContactsView.NULL;
    }


    //---------联系人列表的业务的开始执行----------
    public void loadContacts() {
        HxContactsManager.getsInstance().getContacts();
    }

    public void deleteContact(String hxId) {
        HxContactsManager.getsInstance().asyncDeleteContact(hxId);
    }

    //---------联系人列表的业务的结束----------

    /**
     * 当联系人发生变化的时候刷新联系人
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxRefreshEvent event) {
        if (event.changed) {
            getView().setContacts(event.contacts);
        }
        getView().refreshContacts();
    }

    /**
     * 删除联系人失败
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        if (event.type != HxEventType.DELETE_CONTACT) return;
        getView().deleteContact(event.toString());
    }


}
