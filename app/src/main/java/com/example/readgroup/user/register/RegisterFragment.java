package com.example.readgroup.user.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.readgroup.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public class RegisterFragment extends DialogFragment implements RegisterView {

    @BindView(R.id.edit_username)
    TextInputEditText editUsername;
    @BindView(R.id.edit_password)
    TextInputEditText editPassword;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.button_confirm)
    Button buttonConfirm;

    private Unbinder unbinder;
    private RegisterPresenter registerPresenter;
    private static final String TAG = "RegisterFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPresenter = new RegisterPresenter();
        registerPresenter.onCreat();
        registerPresenter.atteachView(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.button_confirm)
    public void register() {
        String username = editUsername.getText().toString().trim();
        String userpwd = editPassword.getText().toString().trim();
        registerPresenter.register(username, userpwd);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        registerPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerPresenter.onDestory();
    }

    /********
     * start View interface
     ********/
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
    public void showMessage(String s) {
        Log.i(TAG, "showMessage: "+s);
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    /********end  View interface ********/
}
