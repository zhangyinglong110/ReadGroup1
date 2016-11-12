package com.example.apphx.presention.contact.search;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxContactManager;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxSearchContactEvent;
import com.example.apphx.model.event.HxSimpleEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class HxSearchContactsPresenter extends MvpPresenter<HxSearchContactsView> {

    private final HxContactManager hxContactManager;

    public HxSearchContactsPresenter() {
        hxContactManager = HxContactManager.getInstance();
    }

    @Override
    public HxSearchContactsView getNullObject() {
        return HxSearchContactsView.NULL;
    }

    /**
     * 搜索联系人
     *
     * @param query
     */
    public void searchContact(String query) {
        getView().startLoading();
        hxContactManager.asyncSearchContact(query);
    }

    /**
     * 发送一个邀请通知
     *
     * @param toHxId
     */
    public void sendInvite(String toHxId) {
        // 如果已经是好友
        if (hxContactManager.isFriend(toHxId)) {
            getView().showAlreadyIsFriend();
            return;
        }
        getView().startLoading();
        hxContactManager.asyncSendInvite(toHxId);
    }


    //----------搜索好友的事件--------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSearchContactEvent event) {
        getView().stopLoading();

        if (event.isSuccess) {
            getView().showContacts(event.contacts);

            if (event.contacts.size() == 0) {
                getView().showSearchError("No match result!");
            }
        } else {
            getView().showSearchError(event.errorMeassage);
        }
    }
    //----------搜索好友的事件--------------------


    //--------------发送好友邀请的事件--------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent e) {
        if (e.type != HxEventType.SEND_INVITE) return;
        getView().stopLoading();
        getView().showSendInvaiteResult(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent e) {
        if (e.type != HxEventType.SEND_INVITE) return;
        getView().stopLoading();
        getView().showSendInvaiteResult(false);
    }


    //-------------好友邀请的事件--------------------
}
