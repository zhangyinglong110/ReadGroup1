package com.example.readgroup.network.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class BookInfoResult {

    private boolean success;
    private String error;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private BookEntity book;
        private List<UserEntity> likes;

        public BookEntity getBook() {
            return book;
        }

        public List<UserEntity> getLikes() {
            return likes;
        }
    }

}
