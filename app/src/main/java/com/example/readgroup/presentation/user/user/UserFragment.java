package com.example.readgroup.presentation.user.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.readgroup.R;
import com.example.readgroup.network.entity.BookEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class UserFragment extends Fragment implements UserView {


    @BindView(R.id.image_avatar)
    ImageView imageAvatar;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.button_logout)
    Button buttonLogout;
    @BindView(R.id.text_likes_header)
    TextView textLikesHeader;
    @BindView(R.id.list_book)
    ListView listBook;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //-------------start 视图层的表现-------------------------
    @Override
    public void refreshUser(String hxId) {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showUpdateAvararFail(String msg) {

    }

    @Override
    public void setBooks(List<BookEntity> books) {

    }

    @Override
    public void setRefreshing(boolean refreshing) {

    }

    @Override
    public void showRefreshFail(String msg) {

    }
    //-------------end 视图层的表现-------------------------
}
