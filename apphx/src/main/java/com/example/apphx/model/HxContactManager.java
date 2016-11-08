package com.example.apphx.model;

import android.util.Log;

import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxRefreshEvent;
import com.example.apphx.model.event.HxSearchContactEvent;
import com.example.apphx.model.repository.ILocalUsersRepo;
import com.example.apphx.model.repository.IRemoteUserRepo;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

/**
 * 环信联系人管理
 * <p/>
 * MVP的model:主要负责业务处理,且将结果通过EventBus发送到Presenter中去处理
 * <p/>
 * 作者：yuanchao on 2016/10/17 0017 10:03
 * 邮箱：yuanchao@feicuiedu.com
 */
public class HxContactManager implements EMContactListener, EMConnectionListener {

    private static HxContactManager sInstance;

    public static HxContactManager getInstance() {
        if (sInstance == null) {
            sInstance = new HxContactManager();
        }
        return sInstance;
    }

    private List<String> contacts; // 联系人列表集合
    private String currentUserId;

    private final EMContactManager emContactManager;
    private final EventBus eventBus;
    private final ExecutorService executorService;
    private static final String TAG = "HxContactManager";


    private HxContactManager() {

        // EventBus
        eventBus = EventBus.getDefault();
        // 线程池
        executorService = Executors.newSingleThreadExecutor();
        // 环信连接监听
        EMClient.getInstance().addConnectionListener(this);
        // 环信联系人相关操作SDK
        emContactManager = EMClient.getInstance().contactManager();
        emContactManager.setContactListener(this);
    }


    // start-interface: EMConnectionListener
    @Override
    public void onConnected() {
        if (contacts == null) {
            asyncGetContactsFromServer();
        }
    }

    @Override
    public void onDisconnected(int i) {
    }// end-interface: EMConnectionListener

    /**
     * 获取联系人
     */
    public void getContacts() {
        // 已获取过联系人(不用重复去获取)
        if (contacts != null) {
            notifyContactsRefresh();
        }
        // 还未获取过联系人
        else {
            asyncGetContactsFromServer();
        }
    }

    //远程仓库(一般主要指应用的服务器)
    private IRemoteUserRepo remoteUserRepo;
    //本地仓库(指的是本地仓库的换粗数据)
    private ILocalUsersRepo localUsersRepo;

    //初始化远程仓库
    public HxContactManager initRemoteUserRepo(IRemoteUserRepo remoteUserRepo) {
        this.remoteUserRepo = remoteUserRepo;
        return this;
    }

    //初始化本地仓库的操作
    public HxContactManager initLocalUserRepo(ILocalUsersRepo localUsersRepo) {
        this.localUsersRepo = localUsersRepo;
        return this;
    }

    /**
     * 搜索好友功能
     * </p>
     * 环信不提供搜索功能，搜索完全由APP和应用服务器实现
     *
     * @param uasename
     */
    public void asyncSearchContact(final String uasename) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //上应用服务器获取数据,还没用应用服务器，(到远程用户仓库去取数据)
                try {
                    List<EaseUser> user = remoteUserRepo.queryByName(uasename);
                    //缓存到本地 ，只是知道要缓存(只要见过的用户EaseUser，都应该缓存起来)
                    localUsersRepo.saveAll(user);
                    //将结果发送到presenter
                    eventBus.post(new HxSearchContactEvent(user));
                } catch (Exception e) {
                    Log.i(TAG, "asyncSearchContact: error" + e.getMessage());
                    eventBus.post(new HxSearchContactEvent(e.getMessage()));
                }
            }
        };
        executorService.submit(runnable);
    }


    /**
     * 删除联系人,如果删除成功，会自己触发{@link #onContactDeleted(String)}
     * <p/>
     * 注意：A将B删除了, B客户端的{@link #onContactDeleted(String)}也会触发
     *
     * @param hxId 对方的环信id
     */
    public void asyncDeleteContact(final String hxId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    emContactManager.deleteContact(hxId);
                } catch (HyphenateException e) {
                    Timber.e(e, "deleteContact");
                    // 删除失败
                    eventBus.post(new HxErrorEvent(HxEventType.DELETE_CONTACT, e));
                }
            }
        };
        executorService.submit(runnable);
    }


    public void setCurrentUser(String hxId) {
        this.currentUserId = hxId;
    }

    public void reset() {
        contacts = null;
        currentUserId = null;
    }


    private void asyncGetContactsFromServer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    contacts = emContactManager.getAllContactsFromServer();
                    notifyContactsRefresh();
                } catch (HyphenateException e) {
                    Timber.e(e, "asyncGetContactsFromServer");
                }
            }
        };
        executorService.submit(runnable);
    }

    private void notifyContactsRefresh() {
        List<String> currentContacts;
        if (contacts == null) {
            currentContacts = Collections.emptyList();
        } else {
            currentContacts = new ArrayList<>(contacts);
        }
        eventBus.post(new HxRefreshEvent(currentContacts));
    }

    // start contact ContactListener -------------------------
    // 添加联系人
    @Override
    public void onContactAdded(String hxId) {
        Timber.d("onContactAdded %s", hxId);
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
        Timber.d("onContactDeleted %s", hxId);
        if (contacts == null) {
            asyncGetContactsFromServer();
        } else {
            contacts.remove(hxId);
            notifyContactsRefresh();
        }
    }

    // 收到好友邀请
    @Override
    public void onContactInvited(String hxId, String reason) {
        Timber.d("onContactInvited %s, reason: %s", hxId, reason);
    }

    // 好友请求被同意
    @Override
    public void onContactAgreed(String hxId) {
        Timber.d("onContactAgreed %s", hxId);

    }

    // 好友请求被拒绝
    @Override
    public void onContactRefused(String hxId) {
        Timber.d("onContactRefused %s", hxId);
    }

    // end contact ContactListener -------------------------
}
