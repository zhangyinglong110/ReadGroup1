package com.example.readgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.apphx.model.HxUserManager;
import com.example.readgroup.user.login.LoginFragment;
import com.example.readgroup.user.register.RegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.button_register)
    Button buttonRegister;
    @BindView(R.id.button_login)
    Button buttonLogin;

    private Unbinder unbinder;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HxUserManager.getInstance().isLogin()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_splash);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login)
    public void showLoginDialog() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        loginFragment.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.button_register)
    public void showRegisterDialog() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        registerFragment.show(getSupportFragmentManager(), null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
