package com.example.readgroup.presentation.book;

import android.util.Log;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.readgroup.network.BombClient;
import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.event.GetBooksEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class BooksPresenter extends MvpPresenter<BookView> {
    private List<BookEntity> books;
    private static final String TAG = "BooksPresenter";

    @Override
    public BookView getNullObject() {
        return BookView.NULL;
    }


    /**
     * 默认去加载数据
     *
     * @param triggerByUser
     */
    public void getBooks(boolean triggerByUser) {
        if (!triggerByUser) {
            Log.i(TAG, "getBooks: 首页的第一次数据1：");
            if (books != null) {
                getView().setBooks(books);
                Log.i(TAG, "getBooks: 首页的第一次数据2：");
                return;
            }
            getView().setRefreshing(true);
        }
        BombClient.getsInstance().asyncGetBooks();
        Log.i(TAG, "getBooks: 首页的第一次数据3：");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetBooksEvent event) {
        getView().setRefreshing(false);
        if (event.success) {
            books = event.books;
            getView().setBooks(books);
        } else {
            getView().setRefreshFail(event.errorMessage);
        }
    }

}
