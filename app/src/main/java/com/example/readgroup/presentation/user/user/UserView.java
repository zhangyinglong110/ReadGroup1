package com.example.readgroup.presentation.user.user;

import com.example.apphx.basemvp.MvpView;
import com.example.readgroup.network.entity.BookEntity;

import java.util.List;

/**
 * 用户中心的视图表现层
 * Created by Administrator on 2016/11/15 0015.
 */

public interface UserView extends MvpView {

    void refreshUser(String hxId);

    void startLoading();

    void stopLoading();

    void showUpdateAvararFail(String msg);

    void setBooks(List<BookEntity> books);

    void setRefreshing(boolean refreshing);

    void showRefreshFail(String msg);

    UserView NULL = new UserView() {
        @Override
        public void refreshUser(String hxId) {

        }

        @Override
        public void startLoading() {

        }

        @Override
        public void stopLoading() {

        }

        @Override
        public void showUpdateAvararFail(String msg) {

        }

        @Override
        public void setBooks(List<BookEntity> books) {

        }

        @Override
        public void setRefreshing(boolean refreshing) {

        }

        @Override
        public void showRefreshFail(String msg) {

        }
    };
}
