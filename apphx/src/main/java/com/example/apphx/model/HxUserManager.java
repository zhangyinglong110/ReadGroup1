package com.example.apphx.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.apphx.model.event.HxDisconnecEvent;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxSimpleEvent;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

/**
 * Model层, 环信用户基本功能管理，登录、注册、登出
 * <p>
 */
public class HxUserManager implements EMConnectionListener {

    private static HxUserManager sInstance;

    // 当前登录用户的环信Id
    private String currentUserId;

    public static HxUserManager getInstance() {
        if (sInstance == null) {
            sInstance = new HxUserManager();
        }
        return sInstance;
    }

    private final EMClient emClient;
    private final ExecutorService executorService; // 用于当前业务处理内，业务操作的线程池
    private final EventBus eventBus;
    private static final String TAG = "HxUserManager";

    private HxUserManager() {
        emClient = EMClient.getInstance();
        eventBus = EventBus.getDefault();
        executorService = Executors.newSingleThreadExecutor();
        emClient.addConnectionListener(this);
    }


    /**
     * 环信异步注册(用于测试,后期将通过自己应用服务进行注册)
     */
    public void asyncRegister(@NonNull final String hxId, @NonNull final String password) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    emClient.createAccount(hxId, password);
                    Timber.d("%s RegisterHx success", hxId);
                    // 成功了
                    eventBus.post(new HxSimpleEvent(HxEventType.REGISTER));
                } catch (HyphenateException e) {
                    Timber.d("RegisterHx fail");
                    // 失败了
                    eventBus.post(new HxErrorEvent(HxEventType.REGISTER, e));
                }
            }
        };
        // 提交，线程池处理此Runnable
        executorService.submit(runnable);
    }

    /**
     * 环信异步登录
     */
    public void asyncLogin(@NonNull final String hxId, @NonNull final String password) {
        emClient.login(hxId, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, hxId + "  LoginHx success ");
                setCurrentUserId(hxId);
                eventBus.post(new HxSimpleEvent(HxEventType.LOGIN));
            }

            @Override
            public void onError(int code, String message) {
                Timber.d("%s LoginHx error, code is %s.", hxId, code);
                eventBus.post(new HxErrorEvent(HxEventType.LOGIN, code, message));
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    //------start  connected-----
    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected(int error) {
        switch (error) {
            //账号被移除，或者是在其他设备上登录的时候，当前账号直接被挤掉
            case EMError.USER_REMOVED:
            case EMError.USER_LOGIN_ANOTHER_DEVICE:
                setCurrentUserId(null);
                eventBus.post(new HxDisconnecEvent(error));
                break;
            default:
                break;
        }

    }
    //------end  connected-----


    private void setCurrentUserId(String hxID) {
        currentUserId = hxID;
        if (hxID == null) {
            HxContactManager.getInstance().reset();
        } else {
            HxContactManager.getInstance().setCurrentUser(hxID);
        }
    }


    public boolean isLogin() {
        // 返回是否登录过环信，登录成功后，只要没调logout方法，这个方法的返回值一直是true
        // emClient.isLoggedInBefore();

        // emClient.getCurrentUser() 行为难以预测，所以自己写一个变量控制

        return currentUserId != null;
    }

    /**
     * 等出功能
     */
    public void asyncLogout(String hxId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                emClient.logout(true);
                setCurrentUserId(null);
            }
        };
        executorService.submit(runnable);
    }
}
