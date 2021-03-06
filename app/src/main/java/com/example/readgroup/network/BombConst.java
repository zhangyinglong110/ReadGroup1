package com.example.readgroup.network;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public interface BombConst {

    // 统一的Bomb请求头
    String HEADER_SESSION_TOKEN = "X-Bmob-Session-Token";
    String HEADER_APPLICATION_ID = "X-Bmob-Application-Id";
    String HEADER_REST_API_KEY = "X-Bmob-REST-API-Key";
    String HEADER_CONTENT_TYPE = "Content-Type";


    // Bomb请求头的值
    String CONTENT_TYPE_JSON = "application/json";
    String APPLICATION_ID = "e54c192bdb057b18eda0a5032da883e8";
    String REST_API_KEY = "d04d49e3ed57dac2f3557592540868fb";


    // Note: 服务器有1分钟的数据缓存，如果连续两次请求参数一样，下次请求会返回缓存数据
    String BOOKS_URL = "http://cloud.bmob.cn/5a3f440fff51573e/books?time=%s";

    //获取书籍的一个详情数据
    String BOOK_INFO_URL = "http://cloud.bmob.cn/5a3f440fff51573e/getBookInfo?bookId=%s&time=%s";

    //收藏和取消收藏书籍
    String BOOK_LIKE_URL = " http://cloud.bmob.cn/5a3f440fff51573e/changeLike?bookId=%s&userId=%s&action=%s";

    //上传文件
    String UPLOAD_FILE_URL = "https://api.bmob.cn/2/files/avatar.jpeg";

    //获取用户收藏过得书籍
    String USER_LIKES_URL = "http://cloud.bmob.cn/5a3f440fff51573e/getUserLikes?userId=%s&time=%s";

    String UPDATE_USER_URL = "https://api.bmob.cn/1/users/%s";


}
