package com.example.readgroup.presentation.user.user;

import com.example.apphx.basemvp.MvpPresenter;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class UserPresenter extends MvpPresenter<UserView> {
    @Override
    public UserView getNullObject() {
        return UserView.NULL;
    }


}
