package com.example.readgroup.presentation.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readgroup.HomeActivity;
import com.example.readgroup.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public class LoginFragment extends DialogFragment implements LoginView {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_username)
    TextInputEditText editUsername;
    @BindView(R.id.edit_password)
    TextInputEditText editPassword;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.button_confirm)
    Button buttonConfirm;

    private Unbinder unbinder;
    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter();
        loginPresenter.onCreat();
        loginPresenter.atteachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.button_confirm)
    public void login() {
        String username = editUsername.getText().toString().trim();
        String userpwd = editPassword.getText().toString().trim();
        loginPresenter.login(username, userpwd);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        loginPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestory();

    }

    /***
     * statrt  View Interface
     ***/
    @Override
    public void showLoading() {
        setCancelable(false);
        buttonConfirm.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        setCancelable(true);
        progressBar.setVisibility(View.GONE);
        buttonConfirm.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        getActivity().startActivity(intent);
//        getActivity().finish();
    }

    /***end  View Interface***/
}
