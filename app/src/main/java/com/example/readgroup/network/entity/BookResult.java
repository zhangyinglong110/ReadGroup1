package com.example.readgroup.network.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class BookResult {

    private boolean success;
    private String error;
    private List<BookEntity> data;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public List<BookEntity> getData() {
        return data;
    }
}
