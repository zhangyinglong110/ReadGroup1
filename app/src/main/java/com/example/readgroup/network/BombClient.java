package com.example.readgroup.network;

import android.util.Log;

import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.entity.BookInfoResult;
import com.example.readgroup.network.entity.BookResult;
import com.example.readgroup.network.entity.LikeResult;
import com.example.readgroup.network.event.ChangeLikeEvent;
import com.example.readgroup.network.event.GetBookInfoEvenet;
import com.example.readgroup.network.event.GetBooksEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    private final OkHttpClient okhhtClinet;
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

        okhhtClinet = new OkHttpClient.Builder()
                .addInterceptor(new BombInterceptor())
                .addNetworkInterceptor(loggingInterceptor)
                .build();
    }


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
                eventBus.post(event);
            }
        });
    }

    /**
     * 点击后异步获取图书的一个详情
     *
     * @param objectId
     */
    public void asyncGetBookInfo(String objectId) {
        Call call = getBooksDtialCall(objectId);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GetBookInfoEvenet event = new GetBookInfoEvenet(e.getMessage());
                eventBus.post(event);
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handleFailedReponse(response);
                }
                BookInfoResult bookInfoResult = handleSuccessResponse(response, BookInfoResult.class);
                GetBookInfoEvenet evenet = new GetBookInfoEvenet(bookInfoResult.getData().getLikes(),
                        bookInfoResult.getData().getBook());
                Log.i(TAG, "onResponse: ------" + bookInfoResult.getData().getLikes().size());
                Log.i(TAG, "onResponse: ------" + bookInfoResult.getData().getLikes().toString());
                eventBus.post(evenet);
            }
        });
    }

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
                if (!response.isSuccessful()) {
                    handleFailedReponse(response);
                }
                LikeResult likeResult = handleSuccessResponse(response, LikeResult.class);
                ChangeLikeEvent event;
                if (likeResult.isSuccess()) {
                    event = new ChangeLikeEvent(isLike, bookEntity);
                } else {
                    event = new ChangeLikeEvent(isLike, likeResult.getError());
                }
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
        Request requst = new Request
                .Builder()
                .url(url)
                .build();
        return okhhtClinet.newCall(requst);
    }


    /**
     * 图书详情
     *
     * @return
     */
    public Call getBooksDtialCall(String objectId) {
        String url = String.format(BOOK_INFO_URL, objectId, System.currentTimeMillis());
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        Log.i(TAG, "getBooksDtialCall: -------------:" + url);
        return okhhtClinet.newCall(request);
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
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        return okhhtClinet.newCall(request);
    }


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
