package com.example.readgroup.presentation.book.booklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.readgroup.R;
import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.presentation.book.bookinfo.BookInfoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class BooksFragment extends Fragment implements BookView {

    @BindView(R.id.list_book)
    ListView listBook;

    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;

    private Unbinder unbinder;
    private BooksPresenter presenter;
    private BooksAdapter booksAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new BooksPresenter();
        presenter.onCreat();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        presenter.atteachView(this);

        booksAdapter = new BooksAdapter();
        listBook.setAdapter(booksAdapter);
        layoutRefresh.setColorSchemeResources(R.color.brown);
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getBooks(true);
            }
        });
        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookEntity bookEntity = booksAdapter.getItem(position);
                Intent intent = BookInfoActivity.getStartIntent(getContext(), bookEntity);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getBooks(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
    }

    //-----------Start视图层的表现--------------------
    @Override
    public void setBooks(List<BookEntity> books) {
        booksAdapter.setBooks(books);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        layoutRefresh.setRefreshing(refreshing);
    }

    @Override
    public void setRefreshFail(String msg) {
        String info = getString(R.string.book_error_refreshing_fail, msg);
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

    //-----------End--------------------
}
