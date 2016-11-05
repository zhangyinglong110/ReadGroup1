package com.example.apphx.model;

import android.util.Log;

import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxRefreshEvent;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 联系人列表的业务层代码(Model)
 * Created by Administrator on 2016/11/3 0003.
 */

public class HxContactsManager implements EMContactListener, EMConnectionListener {

    private static final String TAG = "HxContactsManager";

    private static HxContactsManager sInstance;

    private final EventBus eventBus;

    private final EMContactManager emContactManager;

    private List<String> contacts;

    private String currentUserId;

    private final ExecutorService executorService;


    public static HxContactsManager getsInstance() {
        if (sInstance == null) {
            sInstance = new HxContactsManager();
        }
        return sInstance;
    }

    private HxContactsManager() {
        eventBus = EventBus.getDefault();
        emContactManager = EMClient.getInstance().contactManager();
        executorService = Executors.newSingleThreadExecutor();
        emContactManager.setContactListener(this);
        EMClient.getInstance().addConnectionListener(this);
    }

    /**
     * 获取联系人
     */
    public void getContacts() {
        if (contacts != null) {
            notifyContactsRefresh();
        } else {
            notifyContactsRefresh();
        }
    }

    /**
     * 删除联系人
     *
     * @param hxId 根据联系人的ID删除联系人
     */
    public void asyncDeleteContact(final String hxId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    emContactManager.deleteContact(hxId);
                } catch (HyphenateException e) {
                    eventBus.post(new HxErrorEvent(HxEventType.DELETE_CONTACT, e));
                }
            }
        };
        executorService.submit(runnable);
    }


    public void setCurrentUser(String hxId) {
        this.currentUserId = hxId;
    }


    //异步获取联系人列表
    private void asyncGetContactsFromServer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    contacts = emContactManager.getAllContactsFromServer();
                    notifyContactsRefresh();
                } catch (HyphenateException e) {
                    Log.i(TAG, "run: asyncGetContactsFromServer 获取联系人异常 ");
                }
            }
        };
        executorService.submit(runnable);
    }


    /**
     * 刷新数据
     */
    private void notifyContactsRefresh() {
        List<String> currentContacts;
        if (contacts == null) {
            currentContacts = Collections.emptyList();
        } else {
            currentContacts = new ArrayList<>(contacts);
        }
        eventBus.post(new HxRefreshEvent(currentContacts));
    }


    //重置联系人
    public void reset() {

        contacts = null;
        currentUserId = null;
    }

    //------------start Contacts interface----------------

    //添加新的联系人
    @Override
    public void onContactAdded(String hxId) {
        if (contacts == null) {
            asyncGetContactsFromServer();
        } else {
            contacts.add(hxId);
            notifyContactsRefresh();
        }
    }

    // 删除联系人
    @Override
    public void onContactDeleted(String hxId) {
        if (contacts == null) {
            asyncGetContactsFromServer();
        } else {
            contacts.remove(hxId);
            notifyContactsRefresh();
        }
    }

    // 接受还有的邀请
    @Override
    public void onContactInvited(String s, String s1) {

    }

    //同意联系人的添加
    @Override
    public void onContactAgreed(String s) {

    }

    //拒绝联系人的添加
    @Override
    public void onContactRefused(String s) {

    }

    //------------end Contacts interface----------------


    //------------start Connection interface----------------

    @Override
    public void onConnected() {
        if (contacts == null) {
            asyncGetContactsFromServer();
        }

    }

    @Override
    public void onDisconnected(int i) {

    }

    //------------end Connection interface----------------

}
