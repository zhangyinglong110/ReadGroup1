package com.example.readgroup.presentation.book.bookinfo;


import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxContactManager;
import com.example.apphx.model.HxUserManager;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxSimpleEvent;
import com.example.readgroup.network.BombClient;
import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.entity.UserEntity;
import com.example.readgroup.network.event.ChangeLikeEvent;
import com.example.readgroup.network.event.GetBookInfoEvenet;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

class BookInfoPresenter extends MvpPresenter<BookInfoView> {


    public void getBookInfo(String bookId, boolean triggerByUser) {
        if (!triggerByUser) {
            getView().setRefreshing(true);
        }
        BombClient.getsInstance()
                .asyncGetBookInfo(bookId);
    }

    public void changeLike(BookEntity bookEntity, boolean isLike) {

        getView().setRefreshing(true);
        String userId = HxUserManager.getInstance().getCurrentUserId();
        BombClient.getsInstance()
                .asyncChangLike(isLike, bookEntity, userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeLikeEvent event) {
        if (event.success) {


            getView().showLikeActionSuccess(event.isLike);
            BombClient.getsInstance()
                    .asyncGetBookInfo(event.bookEntity.getObjectId());
        } else {
            getView().setRefreshing(false);
            getView().showLikeActionFail(event.isLike, event.errorMessage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetBookInfoEvenet event) {

        getView().setRefreshing(false);

        String userId = HxUserManager.getInstance().getCurrentUserId();

        if (event.success) {
            getView().showBookInfo(event.book, event.likes);


            if (event.likes == null) {
                getView().toggleLike(false);
                return;
            }

            boolean hasUser = false;

            for (UserEntity userEntity : event.likes) {
                if (userId.equals(userEntity.getObjectId())) {
                    hasUser = true;
                    break;
                }
            }

            getView().toggleLike(!hasUser);
        } else {
            getView().showGetBookInfoFail(event.errorMessage);
        }
    }

    public void sendInvite(String toHxId) {

        HxContactManager.getInstance()
                .asyncSendInvite(toHxId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent e) {
        if (e.type != HxEventType.SEND_INVITE) return;
        getView().showSendInviteResult(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent e) {
        if (e.type != HxEventType.SEND_INVITE) return;
        getView().showSendInviteResult(false);
    }

    @Override
    public BookInfoView getNullObject() {
        return BookInfoView.NULL;
    }
}
