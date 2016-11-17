package com.example.readgroup.network.entity;

import java.util.List;

/**
 * 书籍收藏回来的数据结果
 * Created by Administrator on 2016/11/16 0016.
 */

public class UserLikeResult {

    public boolean success;
    public String error;
    public List<BookEntity> data;

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
