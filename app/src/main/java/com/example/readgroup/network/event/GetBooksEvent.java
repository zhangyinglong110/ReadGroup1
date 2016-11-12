package com.example.readgroup.network.event;

import com.example.readgroup.network.entity.BookEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class GetBooksEvent {
    public final boolean success;

    public final String errorMessage;

    public final List<BookEntity> books;

    public GetBooksEvent(boolean success, String errorMessage, List<BookEntity> books) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.books = books;
    }
}
