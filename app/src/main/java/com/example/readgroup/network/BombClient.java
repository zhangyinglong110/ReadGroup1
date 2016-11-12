package com.example.readgroup.network;

import com.example.readgroup.network.entity.BookResult;
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
