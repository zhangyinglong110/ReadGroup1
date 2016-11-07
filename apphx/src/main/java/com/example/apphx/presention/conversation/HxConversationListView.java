package com.example.apphx.presention.conversation;

import com.example.apphx.basemvp.MvpView;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public interface HxConversationListView extends MvpView {
    /**
     * 刷新会话列表试图
     */
    void refreshConversations();


    /**
     * 创建一个空对象
     */
    HxConversationListView NULL = new HxConversationListView() {
        @Override
        public void refreshConversations() {

        }
    };
}
