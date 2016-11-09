package com.example.apphx.presention.contact.search;

import com.example.apphx.basemvp.MvpView;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 试图接口层的一个表现试图
 * Created by Administrator on 2016/11/8 0008.
 */

public interface HxSearchContactsView extends MvpView {

    void startLoading();

    void stopLoading();

    /**
     * 显示搜索的结果，一个消息列表
     *
     * @param contacts
     */
    void showContacts(List<EaseUser> contacts);

    /**
     * 显示搜索错误的信息
     *
     * @param error
     */
    void showSearchError(String error);


    /**
     * 显示发送邀请所返回的一个结果
     *
     * @param success
     */
    void showSendInvaiteResult(boolean success);

    /**
     * 显示已经是好友的操作
     */
    void showAlreadyIsFriend();

    HxSearchContactsView NULL = new HxSearchContactsView() {
        @Override
        public void startLoading() {

        }

        @Override
        public void stopLoading() {

        }

        @Override
        public void showContacts(List<EaseUser> contacts) {

        }

        @Override
        public void showSearchError(String error) {

        }

        @Override
        public void showSendInvaiteResult(boolean success) {

        }

        @Override
        public void showAlreadyIsFriend() {

        }
    };
}
