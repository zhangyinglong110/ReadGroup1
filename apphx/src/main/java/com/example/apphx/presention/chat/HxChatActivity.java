package com.example.apphx.presention.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.apphx.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class HxChatActivity extends AppCompatActivity {

    /**
     * 进入聊天界面需要传递的两个参数
     *
     * @param context 当前类对象
     * @param chatId  聊天的目标ID
     */
    public static void open(Context context, String chatId) {
        Intent intent = new Intent(context, HxChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE); //单聊
        intent.putExtra(EaseConstant.EXTRA_USER_ID, chatId);
        context.startActivity(intent);
        //一旦进入聊天的界面就取消通知栏的消息
        EaseUI.getInstance().getNotifier().reset();

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hx_chat);
        addChatFragment();
    }

    private void addChatFragment() {
        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, 0);
        String chatID = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        HxChatFragment hxChatFragment = HxChatFragment.getInstance(chatType, chatID);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, hxChatFragment).commit();
    }
}
