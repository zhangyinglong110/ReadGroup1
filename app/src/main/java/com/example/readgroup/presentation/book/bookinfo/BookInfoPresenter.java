package com.example.readgroup.presentation.book.bookinfo;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxUserManager;
import com.example.readgroup.network.BombClient;
import com.example.readgroup.network.entity.UserEntity;
import com.example.readgroup.network.event.GetBookInfoEvenet;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class BookInfoPresenter extends MvpPresenter<BookInfoView> {


    @Override
    public BookInfoView getNullObject() {
        return BookInfoView.NULL;
    }

    /**
     * 获取图书详情
     *
     * @param bookId
     * @param triggerByUser
     */
    public void getBookInfo(String bookId, boolean triggerByUser) {
        if (!triggerByUser) {
            getView().setRefreshing(true);
        }
        BombClient.getsInstance().asyncGetBookInfo(bookId);
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
                hasUser = true;
                break;
            }
            getView().toggleLike(!hasUser);
        } else {
            getView().showGetBookInfoFail(event.errorMessage);
        }
    }

}
