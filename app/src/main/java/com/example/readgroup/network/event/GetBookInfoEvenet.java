package com.example.readgroup.network.event;

import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.entity.UserEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class GetBookInfoEvenet {

    public final boolean success;
    public final String errorMessage;
    public final List<UserEntity> likes;
    public final BookEntity book;

    public GetBookInfoEvenet(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
        this.likes = null;
        this.book = null;
    }

    public GetBookInfoEvenet(List<UserEntity> likes, BookEntity book) {
        this.success = true;
        this.errorMessage = null;
        this.likes = likes;
        this.book = book;
    }


}
