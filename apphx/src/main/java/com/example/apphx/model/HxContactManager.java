package com.example.apphx.model;

import android.util.Log;

import com.example.apphx.model.entity.InviteMessage;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxRefreshEvent;
import com.example.apphx.model.event.HxRefreshInviteEvent;
import com.example.apphx.model.event.HxSearchContactEvent;
import com.example.apphx.model.event.HxSimpleEvent;
import com.example.apphx.model.repository.ILocalInviteRepo;
import com.example.apphx.model.repository.ILocalUsersRepo;
import com.example.apphx.model.repository.IRemoteUserRepo;
import com.google.gson.Gson;
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


    private List<String> contacts; // 联系人列表集合
    private String currentUserId;
    private final EMContactManager emContactManager;
    private final EventBus eventBus;
    private final ExecutorService executorService;
    private static final String TAG = "HxContactManager";
    private final Gson gson;

    private static HxContactManager sInstance;

    public static HxContactManager getInstance() {
        if (sInstance == null) {
            sInstance = new HxContactManager();
        }
        return sInstance;
    }


    private HxContactManager() {
        // EventBus
        eventBus = EventBus.getDefault();
        // 线程池
        executorService = Executors.newSingleThreadExecutor();
        gson = new Gson();
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
    public void retrieveContacts() {
        // 已获取过联系人(不用重复去获取)
        if (contacts != null) {
            notifyContactsRefresh();
        }
        // 还未获取过联系人
        else {
            asyncGetContactsFromServer();
        }
    }

    List<String> getContacts() {
        return contacts;
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
                try {
                    //上应用服务器获取数据,还没用应用服务器，(到远程用户仓库去取数据)
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
     * 发送一个好友邀请的操作
     *
     * @param hxId
     */
    public void asyncSendInvite(final String hxId) {
        Log.i(TAG, "asyncSendInvite: ------------------" + hxId);
        final EaseUser easeUser = localUsersRepo.getUser(currentUserId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //发送一个添加联系人的操作
                    emContactManager.addContact(hxId, gson.toJson(easeUser));
                    eventBus.post(new HxSimpleEvent(HxEventType.SEND_INVITE));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    eventBus.post(new HxErrorEvent(HxEventType.SEND_INVITE, e));
                }
            }
        };
        executorService.submit(runnable);
    }


    /**
     * 同意接受好友邀请
     * <p>
     * 邀请信息：
     * 发送方的id
     * 接受方的ID
     * 状态
     */
    public void acyncAcceptInvite(final InviteMessage inviteMessage) {
        try {
            //noto:此方法成功，环信会自动触发双方的onContactAdded方法
            emContactManager.acceptInvitation(inviteMessage.getFormId());
            //将邀请信息的状态更新为已同意状态
            inviteMessage.setStatus(InviteMessage.Status.ACCEPTED);
            // TODO: 2016/11/9 0009
            //保存到仓库内
            localInviteRepo.save(inviteMessage);
            //发送事件去表现层去处理
            eventBus.post(new HxSimpleEvent(HxEventType.ACCEPT_INVITE));
        } catch (HyphenateException e) {
            e.printStackTrace();
            Log.i(TAG, "acyncAcceptInvite: 同意接受好友邀请 error");
            eventBus.post(new HxErrorEvent(HxEventType.ACCEPT_INVITE, e));
        }
    }


    /**
     * 拒绝好友邀请
     */
    public void asyncRefuseInvite(final InviteMessage inviteMessage) {
        try {
            emContactManager.declineInvitation(inviteMessage.getFormId());
            inviteMessage.setStatus(InviteMessage.Status.REFUSED);
            // TODO: 2016/11/9 0009 做一个本地邀请的信息仓库，用来保存所有的邀请信息
            //保存到仓库内
            localInviteRepo.save(inviteMessage);
            //发送事件去表现层去处理
            eventBus.post(new HxSimpleEvent(HxEventType.REFUSE_INVITE));
        } catch (HyphenateException e) {
            e.printStackTrace();
            Log.i(TAG, "asyncRefuseInvite: 拒绝好友邀请 error");
            eventBus.post(new HxErrorEvent(HxEventType.REFUSE_INVITE, e));
        }
    }

    /**
     * 获取所有的邀请信息
     */
    public void getInvites() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                notifyInviteRefresh();
            }
        });
    }

    /**
     * 判断是否是好友
     *
     * @param hxId
     * @return
     */
    public boolean isFriend(String hxId) {
        return contacts != null && contacts.contains(hxId);
    }

    public boolean isContact(String hxId) {
        return contacts != null && contacts.contains(hxId);
    }

    /**
     * 从服务器获取联系人
     */
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
        //删除仓库内信息
        localInviteRepo.delete(hxId);
        notifyInviteRefresh();
    }

    /**
     * 收到好友邀请
     * A 向 B发送邀请，B的这个方法会被调用
     */
    @Override
    public void onContactInvited(String hxId, String reason) {
        Timber.d("onContactInvited %s, reason: %s", hxId, reason);
        //取出邀请方的理由
        EaseUser easeUser = gson.fromJson(reason, EaseUser.class);
        localUsersRepo.save(easeUser);


        InviteMessage inviteMessage = new InviteMessage(hxId, currentUserId, InviteMessage.Status.RAW);
        // TODO: 2016/11/9 0009  保存本地邀请信息
        //保存到仓库内
        localInviteRepo.save(inviteMessage);
        /**
         * 在收到好友邀请的时候要刷新
         */
        notifyInviteRefresh();
    }

    /**
     * 刷新邀请信息的界面
     */
    private void notifyInviteRefresh() {
        List<InviteMessage> message = localInviteRepo.getAll();
        Log.i(TAG, "notifyInviteRefresh: -----------" + message.size());
        eventBus.post(new HxRefreshInviteEvent(message));
    }

    // 好友请求被同意
    @Override
    public void onContactAgreed(String hxId) {
        Timber.d("onContactAgreed %s", hxId);
        InviteMessage inviteMessage = new InviteMessage(hxId, currentUserId, InviteMessage.Status.REMOTE_ACCEPED);
        // TODO: 2016/11/10 0010  保存本地邀请信息
        localInviteRepo.save(inviteMessage);
        notifyInviteRefresh();
    }

    // 好友请求被拒绝
    @Override
    public void onContactRefused(String hxId) {
        Timber.d("onContactRefused %s", hxId);
    }

    // end contact ContactListener -------------------------


    //--------------------------START(本地)仓库的开始---------------------------------------
    //远程仓库(一般主要指应用的服务器)
    private IRemoteUserRepo remoteUserRepo;
    //本地仓库(指的是本地仓库的换粗数据)
    private ILocalUsersRepo localUsersRepo;
    //本地邀请好友信息的一个仓库
    private ILocalInviteRepo localInviteRepo;


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

    //初始化本地邀请仓库的操作
    public HxContactManager initLoacalInviteRepo(ILocalInviteRepo localInviteRepo) {
        this.localInviteRepo = localInviteRepo;
        return this;
    }

    //--------------------------END(本地)仓库的开始---------------------------------------


    public void setCurrentUser(String hxId) {
        this.currentUserId = hxId;
        localInviteRepo.setCurrentUser(hxId);
    }

    /**
     * 清空数据
     */
    public void reset() {
        contacts = null;
        currentUserId = null;
        localInviteRepo.setCurrentUser(null);
    }
}
