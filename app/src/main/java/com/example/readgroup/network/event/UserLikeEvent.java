package com.example.readgroup.network.event;

import com.example.readgroup.network.entity.BookEntity;

import java.util.List;

/**
 * 获取收藏的Event事件
 * Created by Administrator on 2016/11/16 0016.
 */

public class UserLikeEvent {
    public final boolean success;
    public final String errorMessage;
    public final List<BookEntity> books;

    public UserLikeEvent(List<BookEntity> books, String errorMessage, boolean success) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.books = books;
    }
}
