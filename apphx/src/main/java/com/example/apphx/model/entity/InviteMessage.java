package com.example.apphx.model.entity;

/**
 * 好友邀请信息的实体
 * <p>
 * 例如：A 加 B为好友
 * B收到邀请，B会存储一条邀请信息，此时为“未处理”状态
 * Created by Administrator on 2016/11/9 0009.
 */

public class InviteMessage {
    /**
     * 发送方环信ID
     */
    private String formId;
    /**
     * 接收方环信ID
     */
    private String toHxId;

    /**
     * 当前消息状态
     */
    private Status status;

    public enum Status {
        /**
         * 未处理
         */
        RAW,
        /**
         * 已同意
         */
        ACCEPTED,
        /**
         * 已拒绝
         */
        REFUSED,
        REMOTE_ACCEPED
    }


    public InviteMessage() {
    }

    public InviteMessage(String formId, String toHxId, Status status) {
        this.formId = formId;
        this.toHxId = toHxId;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getFormId() {
        return formId;
    }

    public String getToHxId() {
        return toHxId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setToHxId(String toHxId) {
        this.toHxId = toHxId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
