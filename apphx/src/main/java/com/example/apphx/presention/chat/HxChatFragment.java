package com.example.apphx.presention.chat;

import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * 环信聊天的界面
 * Created by Administrator on 2016/11/7 0007.
 */

public class HxChatFragment extends EaseChatFragment {


    /**
     * 因为EaseCharFragment需要chatType,chatID,所以提供一个方法，一旦使用当前的Fragment的时候，需要传递这两个参数
     *
     * @param chatType 聊天的类型，（单聊？群聊）
     * @param chatID   聊天的目标是谁
     * @return
     */
    public static HxChatFragment getInstance(int chatType, String chatID) {
        HxChatFragment chatFragment = new HxChatFragment();
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        args.putString(EaseConstant.EXTRA_USER_ID, chatID);
        chatFragment.setArguments(args);
        return chatFragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customUI();
    }

    private void customUI() {
        hideTitleBar();
    }
}
