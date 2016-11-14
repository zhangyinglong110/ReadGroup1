package com.example.readgroup.presentation.book.bookinfo;

import com.example.apphx.basemvp.MvpView;
import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.entity.UserEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public interface BookInfoView extends MvpView {

    /**
     * 设置图书的详情页面
     *
     * @param bookEntity
     * @param likes
     */
    void showBookInfo(BookEntity bookEntity, List<UserEntity> likes);

    /**
     * 设置刷新
     *
     * @param refreshing
     */
    void setRefreshing(boolean refreshing);

    /**
     * 显示设置图书详情失败的信息
     *
     * @param msg
     */
    void showGetBookInfoFail(String msg);

    /**
     * 切换喜欢的
     *
     * @param showLike
     */
    void toggleLike(boolean showLike);


    /**
     * 显示收藏成功的信息
     *
     * @param isLike
     */
    void showLikeActionSuccess(boolean isLike);

    /**
     * 显示收藏失败的信息
     *
     * @param isLike
     * @param msg
     */
    void showLikeActionFail(boolean isLike, String msg);


    /**
     * 显示接受邀请
     *
     * @param success
     */
    void showSendInviteResult(boolean success);


    /**
     * +
     * 返回一个空对象
     */
    BookInfoView NULL = new BookInfoView() {
        @Override
        public void showBookInfo(BookEntity bookEntity, List<UserEntity> likes) {

        }

        @Override
        public void setRefreshing(boolean refreshing) {

        }

        @Override
        public void showGetBookInfoFail(String msg) {

        }

        @Override
        public void toggleLike(boolean showLike) {

        }

        @Override
        public void showLikeActionSuccess(boolean isLike) {

        }

        @Override
        public void showLikeActionFail(boolean isLike, String msg) {

        }

        @Override
        public void showSendInviteResult(boolean success) {

        }
    };
}
