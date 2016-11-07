package com.example.apphx.presention.conversation;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxMessageManager;
import com.example.apphx.model.event.HxNewMsgEvent;
import com.hyphenate.chat.EMConversation;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 会话列表的一个协调者
 * Created by Administrator on 2016/11/7 0007.
 */

public class HxConversationsPresenter extends MvpPresenter<HxConversationListView> {
    @Override
    public HxConversationListView getNullObject() {
        return HxConversationListView.NULL;
    }

    public void deleteConversation(EMConversation conversation, boolean delteMessage) {
        HxMessageManager.getsInstace().deleteConversation(conversation.getUserName(), delteMessage);
        getView().refreshConversations();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxNewMsgEvent event) {
        // 收到会话,刷新会话列表视图
        getView().refreshConversations();
    }


}
