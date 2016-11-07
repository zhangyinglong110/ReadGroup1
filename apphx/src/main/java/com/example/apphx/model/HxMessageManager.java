package com.example.apphx.model;

import com.example.apphx.model.event.HxNewMsgEvent;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 会话列表的一个Model层
 * Created by Administrator on 2016/11/7 0007.
 */

public class HxMessageManager implements EMMessageListener {

    private static HxMessageManager sInstace;
    private final EventBus eventBus;
    private final EMChatManager emChatManager;


    /**
     * 会话列表的单例
     *
     * @return
     */
    public static HxMessageManager getsInstace() {
        if (sInstace == null) {
            sInstace = new HxMessageManager();
        }
        return sInstace;
    }


    private HxMessageManager() {
        eventBus = EventBus.getDefault();
        emChatManager = EMClient.getInstance().chatManager();
        //添加一个会话列表的监听
        emChatManager.addMessageListener(this);
    }


    //删除会话
    public void deleteConversation(String hxID, boolean deleteMessage) {
        emChatManager.deleteConversation(hxID, deleteMessage);
    }


    //----------------start EMMessageListener -----------------------------------------

    /**
     * 接受消息接口，在接收到文本消息，图片，视频，语音，地理位置等会话列表
     *
     * @param list
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        eventBus.post(new HxNewMsgEvent(list));
    }

    // 这个接口只包含命令的消息体(透传消息)，包含命令的消息体通常不对用户展示。
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {
        // 接受到消息体的已读回执, 消息的接收方已经阅读此消息。
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {
        // 收到消息体的发送回执，消息体已经成功发送到对方设备。
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
        // 接受消息发生改变的通知，包括消息ID的改变。消息是改变后的消息。
    }


    //----------------end EMMessageListener -----------------------------------------
}
