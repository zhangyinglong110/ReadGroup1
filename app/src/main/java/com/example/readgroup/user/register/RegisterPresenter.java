package com.example.readgroup.user.register;

import com.example.apphx.model.HxUserManager;
import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxSimpleEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public class RegisterPresenter extends MvpPresenter<RegisterView> {
    @Override
    public RegisterView getNullObject() {
        return RegisterView.NULL;
    }

    public void register(String userid, String userpwd) {
        getView().showLoading();
        HxUserManager.getInstance() .asyncRegister(userid, userpwd);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        if (event.type != HxEventType.REGISTER) return;
        getView().hideLoading();
        getView().showMessage("注册成功！");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        if (event.type != HxEventType.REGISTER) return;
        getView().hideLoading();
        getView().showMessage(event.errorMessage);
    }


}
