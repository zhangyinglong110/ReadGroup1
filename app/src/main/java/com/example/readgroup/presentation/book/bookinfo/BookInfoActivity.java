package com.example.readgroup.presentation.book.bookinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.readgroup.R;
import com.example.readgroup.network.entity.BookEntity;
import com.example.readgroup.network.entity.UserEntity;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class BookInfoActivity extends AppCompatActivity implements BookInfoView {

    private static final String EXTRA_KEY_BOOK_ENTITY = "EXTRA_KEY_BOOK_ENTITY";
    @BindView(R.id.list_likes)
    ListView listLikes;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Unbinder unbinder;

    private BookEntity bookEntity;
    private BookInfoPresenter presenter;
    private LikesAdapter likesAdapter;
    private BookHeaderHolder bookHeaderHolder;
    private int menuId;
    private static final String TAG = "BookInfoActivity";

    public static Intent getStartIntent(Context context, BookEntity bookEntity) {
        Gson gson = new Gson();
        Intent intent = new Intent(context, BookInfoActivity.class);
        intent.putExtra(EXTRA_KEY_BOOK_ENTITY, gson.toJson(bookEntity));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookEntity = new Gson().fromJson(getIntent().getStringExtra(EXTRA_KEY_BOOK_ENTITY), BookEntity.class);
        setContentView(R.layout.activity_book_info);
        ButterKnife.bind(this);
        presenter = new BookInfoPresenter();
        presenter.onCreat();
        presenter.atteachView(this);
        layoutRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getBookInfo(bookEntity.getObjectId(), false);
            }
        }, 100);

    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        likesAdapter = new LikesAdapter(new LikesAdapter.OnInviteContactListener() {
            @Override
            public void onInvite(@NonNull UserEntity userEntity) {
                presenter.sendInvite(userEntity.getObjectId());
            }
        });
        listLikes.setAdapter(likesAdapter);
        View header = LayoutInflater.from(this).inflate(R.layout.partial_header_book, listLikes, false);
        bookHeaderHolder = new BookHeaderHolder(header);
        listLikes.addHeaderView(header);
        layoutRefresh.setColorSchemeResources(R.color.brown);

        //手动下拉刷新的时候的操作
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getBookInfo(bookEntity.getObjectId(), true);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.detachView();
        presenter.onDestory();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != 0) {
            getMenuInflater().inflate(menuId, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_like:
                presenter.changeLike(bookEntity, true);
                break;
            case R.id.menu_dislike:
                presenter.changeLike(bookEntity, false);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    //-----------------------Start 视图层的开始--------------------------------------
    @Override
    public void showBookInfo(BookEntity bookEntity, List<UserEntity> likes) {
        //设置试图上的数据。然后Model层会在数据返回的时候调用
        getSupportActionBar().setTitle(bookEntity.getTitle());
        bookHeaderHolder.bind(bookEntity);
        Log.i(TAG, "showBookInfo: ---" + likes);
        likesAdapter.setData(likes);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        layoutRefresh.setRefreshing(refreshing);
    }

    @Override
    public void showGetBookInfoFail(String msg) {
        String info = getString(R.string.book_error_refresh_book_info, msg);
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toggleLike(boolean showLike) {
        Log.i(TAG, "toggleLike: ----" + showLike);
        menuId = showLike ? R.menu.activity_menu_info_like : R.menu.activity_menu_info_dislike;
        invalidateOptionsMenu();
    }

    @Override
    public void showLikeActionSuccess(boolean isLike) {
        int msgId = isLike ? R.string.book_like_success : R.string.book_dislike_success;
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLikeActionFail(boolean isLike, String msg) {
        int strId = isLike ? R.string.book_error_like : R.string.book_error_dislike;
        String info = getString(strId, msg);
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSendInviteResult(boolean success) {
        if (success) {
            Toast.makeText(this, com.example.apphx.R.string.hx_contact_send_invite_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, com.example.apphx.R.string.hx_contact_send_invite_fail, Toast.LENGTH_SHORT).show();
        }
    }

    //-----------------------End 视图层的开始--------------------------------------
}
