package com.example.readgroup.presentation.user.register;

import com.example.apphx.basemvp.MvpView;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public interface RegisterView extends MvpView{
    void showLoading();

    void hideLoading();

    void showMessage(String s);

    RegisterView NULL = new RegisterView() {
        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void showMessage(String s) {

        }
    };
}
