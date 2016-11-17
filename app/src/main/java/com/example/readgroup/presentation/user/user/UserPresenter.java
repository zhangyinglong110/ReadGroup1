package com.example.readgroup.presentation.user.user;


import android.support.annotation.NonNull;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxMessageManager;
import com.example.apphx.model.HxUserManager;
import com.example.readgroup.network.BombClient;
import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.event.ChangeLikeEvent;
import com.example.readgroup.network.event.UpdateUserEvent;
import com.example.readgroup.network.event.UploadFileEvent;
import com.example.readgroup.network.event.UserLikeEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

class UserPresenter extends MvpPresenter<UserView> {

    private final BombClient bombClient;
    private final HxUserManager hxUserManager;

    private List<BookEntity> likes;

    public UserPresenter() {
        bombClient = BombClient.getsInstance();
        hxUserManager = HxUserManager.getInstance();
    }

    @NonNull
    @Override
    public UserView getNullObject() {
        return UserView.NULL;
    }

    public void updateUser() {
        String hxId = hxUserManager.getCurrentUserId();
        getView().refreshUser(hxId);
    }

    public void updateAvatar(File file) {
        getView().startLoading();
        bombClient.asyncUploadFile(file);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UploadFileEvent event) {
        if (event.success) {
            String hxId = hxUserManager.getCurrentUserId();
            bombClient.asyncUpdateUser(hxId, event.url);
        } else {
            getView().stopLoading();
            getView().showUpdateAvatarFail(event.errorMessage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUserEvent event) {
        getView().stopLoading();

        if (event.success) {
            String hxId = hxUserManager.getCurrentUserId();
            HxMessageManager.getsInstace().sendAvatarUpdateMessage(event.avatar);
//            hxUserManager.updateAvatar(event.avatar);
            getView().refreshUser(hxId);
        } else {
            getView().showUpdateAvatarFail(event.errorMessage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeLikeEvent event) {

        if (likes == null) return;

        if (!event.success) return;

        if (event.isLike) {
            likes.add(event.bookEntity);
            getView().setBooks(likes);
        } else {
            BookEntity target = null;
            for (BookEntity bookEntity : likes) {
                if (bookEntity.getObjectId().equals(event.bookEntity.getObjectId())) {
                    target = bookEntity;
                    break;
                }
            }
            likes.remove(target);

            getView().setBooks(likes);
        }
    }


    public void getLikes(boolean triggerByUser) {

        if (!triggerByUser) {
            if (likes != null) {
                getView().setBooks(likes);
                return;
            }

            getView().setRefreshing(true);
        }

        String userId = HxUserManager.getInstance()
                .getCurrentUserId();
        BombClient.getsInstance()
                .asyncGetUserLikes(userId);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserLikeEvent event) {
        getView().setRefreshing(false);
        if (event.success) {
            likes = event.books;
            getView().setBooks(likes);
        } else {
            getView().showRefreshFail(event.errorMessage);
        }
    }
}
