package com.example.readgroup.network.event;

import com.example.readgroup.network.entity.BookEntity;

/**
 * 加入收藏或者取消收藏的Event事件
 * Created by Administrator on 2016/11/15 0015.
 */

public class ChangeLikeEvent {

    public final boolean success;
    public final String errorMessage;
    public final boolean isLike;
    public final BookEntity bookEntity;

    public ChangeLikeEvent(boolean isLike, String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
        this.isLike = isLike;
        this.bookEntity = null;
    }

    public ChangeLikeEvent(boolean isLike, BookEntity bookEntity) {
        this.success = true;
        this.errorMessage = null;
        this.isLike = isLike;
        this.bookEntity = bookEntity;
    }
}
