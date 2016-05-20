package com.mirrorchannelth.internship;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.service.LoginService;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.model.UserProfile;
import com.mirrorchannelth.internship.net.Connection;
import com.mirrorchannelth.internship.util.FormValidation;
import com.mirrorchannelth.internship.util.WindowsUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Connection.OnConnectionCallBackListener {
    private EditText usernameBox, passwordBox = null;
    private LoginService loginDAO;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
        loginDAO = new LoginService(WebAPI.URL);
    }

    public void initWidget() {
        usernameBox = (EditText) findViewById(R.id.editUsername);
        passwordBox = (EditText) findViewById(R.id.editPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               requestLogin();
            }
        });
    }

    public void requestLogin() {
        //  Validate form input
        boolean isValid = true;
        String password = passwordBox.getText().toString();
        String username = usernameBox.getText().toString();

        if (!FormValidation.isPassword(password)) {
            isValid = false;
            passwordBox.setError(getString(R.string.login_password_error));

            //  Swap progress bar & login button
//            Animator animator = Animator.getInstance();
//            animator.swapView(progressBar, loginBtn);
        }

        if (isValid) {
            //  Hide keyboard
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            progressBar.setVisibility(View.VISIBLE);
            loginDAO.requestLogin(username, password, this);
        }
    }

    @Override
    public void onSuccess(String result) {

        progressBar.setVisibility(View.GONE);
        try {
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                UserProfile userProfile = new UserProfile(response.getJSONObject("result"));
                ShareData.setUserProfile(userProfile);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.login_username_incorrect), getString(R.string.default_label_dialog_button), LoginActivity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLostConnection() {
        progressBar.setVisibility(View.GONE);
        WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.default_message_dialog), getString(R.string.default_label_dialog_button), LoginActivity.this);
    }

    @Override
    public void onUnreachHost() {
        progressBar.setVisibility(View.GONE);
        WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.default_message_dialog), getString(R.string.default_label_dialog_button), LoginActivity.this);
    }
}
