package com.example.readgroup.network.event;

/**
 * 上传文件的Event事件<br/>
 * Created by Administrator on 2016/11/15 0015.
 */

public class UploadFileEvent {
    public final boolean success;
    public final String errorMessage;
    public final String url;

    public UploadFileEvent(boolean success, String errorMessage, String url) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.url = url;
    }
}
