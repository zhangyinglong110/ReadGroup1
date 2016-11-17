package com.example.readgroup.network;

import android.util.Log;

import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.entity.BookInfoResult;
import com.example.readgroup.network.entity.BookResult;
import com.example.readgroup.network.entity.FileResult;
import com.example.readgroup.network.entity.LikeResult;
import com.example.readgroup.network.entity.UserLikesResult;
import com.example.readgroup.network.event.ChangeLikeEvent;
import com.example.readgroup.network.event.GetBookInfoEvenet;
import com.example.readgroup.network.event.GetBooksEvent;
import com.example.readgroup.network.event.UpdateUserEvent;
import com.example.readgroup.network.event.UploadFileEvent;
import com.example.readgroup.network.event.UserLikeEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class BombClient implements BombConst {

    private static BombClient sInstance;
    private static final String TAG = "BombClient";

    public static BombClient getsInstance() {
        if (sInstance == null) {
            sInstance = new BombClient();
        }
        return sInstance;
    }

    private final OkHttpClient okhttpClinet;
    private final EventBus eventBus;
    private final Gson gson;

    private BombClient() {
        eventBus = EventBus.getDefault();
        gson = new Gson();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.d(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okhttpClinet = new OkHttpClient.Builder()
                .addInterceptor(new BombInterceptor())
                .addNetworkInterceptor(loggingInterceptor)
                .build();
    }

    //------------------START-首页数据---------------------------------------------------------------

    /**
     * 异步去获取首页列表的一个数据
     */
    public void asyncGetBooks() {
        Call call = getBooksCall();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GetBooksEvent event = new GetBooksEvent(false, e.getMessage(), null);
                eventBus.post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handleFailedReponse(response);
                }
                BookResult bookResult = handleSuccessResponse(response, BookResult.class);
                GetBooksEvent event = new GetBooksEvent(true, bookResult.getError(), bookResult.getData());
                Log.i(TAG, "onResponse: ----------" + bookResult.getData());
                eventBus.post(event);
            }
        });
    }

    /**
     * 图书列表的首页
     *
     * @return
     */
    public Call getBooksCall() {
        String url = String.format(BOOKS_URL, System.currentTimeMillis());
        Log.i(TAG, "getBooksCall: --------" + url);
        Request requst = new Request
                .Builder()
                .url(url)
                .build();
        return okhttpClinet.newCall(requst);
    }

    //------------------END-首页数据-----------------------------------------------------------------


    //------------------START图书详情----------------------------------------------------------------

    /**
     * 点击后异步获取图书的一个详情
     *
     * @param bookId
     */
    public void asyncGetBookInfo(String bookId) {
        Call call = getBooksDtialCall(bookId);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GetBookInfoEvenet event = new GetBookInfoEvenet(e.getMessage());
                eventBus.post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        handleFailedReponse(response);
                    }

                    BookInfoResult result = handleSuccessResponse(response, BookInfoResult.class);

                    GetBookInfoEvenet event = new GetBookInfoEvenet(result.getData().getLikes(),
                            result.getData().getBook());
                    eventBus.post(event);
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }
        });
    }


    /**
     * 图书详情
     *
     * @return
     */
    public Call getBooksDtialCall(String objectId) {
        String url = String.format(BOOK_INFO_URL, objectId, System.currentTimeMillis());

        Request request = new Request.Builder()
                .url(url)
                .build();
        return okhttpClinet.newCall(request);
    }
    //------------------END图书详情----------------------------------------------------------------


    //---------------------START-----加入收藏或者取消收藏的-------------------------
    public void asyncChangLike(final boolean isLike, final BookEntity bookEntity, final String userId) {
        Call call = getBookLikeCall(bookEntity.getObjectId(), userId, isLike);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangeLikeEvent event = new ChangeLikeEvent(isLike, e.getMessage());
                eventBus.post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        handleFailedReponse(response);
                    }

                    LikeResult result = handleSuccessResponse(response, LikeResult.class);


                    ChangeLikeEvent event;
                    if (result.isSuccess()) {
                        event = new ChangeLikeEvent(isLike, bookEntity);
                    } else {
                        event = new ChangeLikeEvent(isLike, result.getError());
                    }

                    eventBus.post(event);
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }
        });
    }


    /**
     * 加入收藏或者取消收藏的
     *
     * @param bookId
     * @param userId
     * @param isLike
     * @return
     */
    public Call getBookLikeCall(String bookId, String userId, boolean isLike) {
        String action = isLike ? "like" : "dislike";

        String url = String.format(BOOK_LIKE_URL, bookId, userId, action);
        Request request = new Request.Builder()
                .url(url)
                .build();
        return okhttpClinet.newCall(request);
    }


    //---------------------END-----加入收藏或者取消收藏的-------------------------


    //-------------------------START---------------------------
    public void asyncUploadFile(File file) {
        Call call = getUploadFileCall(file);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UploadFileEvent event = new UploadFileEvent(false, e.getMessage(), null);
                eventBus.post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        handleFailedReponse(response);
                    }

                    FileResult fileResult = handleSuccessResponse(response, FileResult.class);
                    UploadFileEvent event = new UploadFileEvent(true, null, fileResult.getUrl());
                    eventBus.post(event);
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }
        });
    }

    /**
     * 上传文件的业务
     *
     * @param file
     * @return
     */
    public Call getUploadFileCall(File file) {
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
        Request request = new Request.Builder()
                .url(UPLOAD_FILE_URL)
                .post(body)
                .build();
        return okhttpClinet.newCall(request);
    }
    //-------------------------END上传文件的业务---------------------------

    //------------------------Start-更新用户信息------------------------------

    public void asyncUpdateUser(String id, final String avatar) {
        Call call = getUpdateUserCall(id, avatar);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UpdateUserEvent event = new UpdateUserEvent(false, e.getMessage(), null);
                eventBus.post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        handleFailedReponse(response);
                    }

                    UpdateUserEvent event = new UpdateUserEvent(true, null, avatar);
                    eventBus.post(event);
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }
        });
    }

    public Call getUpdateUserCall(String id, String avatar) {
        String url = String.format(UPDATE_USER_URL, id);

        String content = "{\"avatar\":\"%s\"}";
        content = String.format(content, avatar);
        RequestBody body = RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON), content);

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        return okhttpClinet.newCall(request);
    }
    //------------------------END-更新用户信息------------------------------


    //-----------------------------Start--获取用户收藏过得数据列表----------------------------------------
    public void asyncGetUserLikes(String userId) {
        Call call = getUserLikesCall(userId);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UserLikeEvent event = new UserLikeEvent(null, e.getMessage(), false);
                eventBus.post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        handleFailedReponse(response);
                    }

                    UserLikesResult result = handleSuccessResponse(response, UserLikesResult.class);

                    UserLikeEvent event = new UserLikeEvent(result.getData(), result.getError(), result.isSuccess());
                    eventBus.post(event);
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }
        });
    }

    public Call getUserLikesCall(String userId) {
        String url = String.format(USER_LIKES_URL, userId, System.currentTimeMillis());
        Request request = new Request.Builder()
                .url(url)
                .build();
        return okhttpClinet.newCall(request);
    }
    //-----------------------------End--获取用户收藏过得数据列表----------------------------------------

    private void handleFailedReponse(Response response) throws IOException {
        String error = response.body().string();
        Timber.e(error);
        throw new IOException(response.code() + ", " + error);
    }

    private <T> T handleSuccessResponse(Response response, Class<T> clazz) throws IOException {
        String content = response.body().string();
        return gson.fromJson(content, clazz);
    }

}
