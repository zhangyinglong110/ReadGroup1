package com.example.readgroup.presentation.book.booklist;

import com.example.apphx.basemvp.MvpView;
import com.example.readgroup.network.entity.BookEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public interface BookView extends MvpView {

    void setBooks(List<BookEntity> books);

    void setRefreshing(boolean refreshing);

    void setRefreshFail(String msg);

    BookView NULL = new BookView() {
        @Override
        public void setBooks(List<BookEntity> books) {

        }

        @Override
        public void setRefreshing(boolean refreshing) {

        }

        @Override
        public void setRefreshFail(String msg) {

        }
    };

}
