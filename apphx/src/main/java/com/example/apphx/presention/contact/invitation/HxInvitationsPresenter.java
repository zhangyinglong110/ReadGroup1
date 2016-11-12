package com.example.apphx.presention.contact.invitation;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxContactManager;
import com.example.apphx.model.entity.InviteMessage;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxRefreshInviteEvent;
import com.example.apphx.model.event.HxSimpleEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class HxInvitationsPresenter extends MvpPresenter<HxInvitationsView> {
    private final HxContactManager hxContactManager;


    public HxInvitationsPresenter() {
        hxContactManager = HxContactManager.getInstance();
    }


    @Override
    public HxInvitationsView getNullObject() {
        return HxInvitationsView.NULL;
    }

    /**
     * 获取所有的邀请信息
     */
    public void getInvites() {
        hxContactManager.getInvites();
    }

    /**
     * 接受好友邀请
     *
     * @param invite
     */
    public void accept(InviteMessage invite) {
        hxContactManager.acyncAcceptInvite(invite);
    }

    /**
     * 拒绝好友邀请
     *
     * @param invite
     */
    public void refuse(InviteMessage invite) {
        hxContactManager.asyncRefuseInvite(invite);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxRefreshInviteEvent event) {
        Timber.d("onEvent HxRefreshInviteEvent");
        getView().refreshInvitations(event.message);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        if (event.type == HxEventType.ACCEPT_INVITE || event.type == HxEventType.REFUSE_INVITE) {
            // 同意或拒绝邀请成功
            getView().refreshInvitations();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        if (event.type == HxEventType.ACCEPT_INVITE || event.type == HxEventType.REFUSE_INVITE) {
            // 同意或拒绝邀请失败
            getView().showActionFail();
        }
    }
}
