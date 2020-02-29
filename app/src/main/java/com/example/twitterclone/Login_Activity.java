package com.example.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTextUsername, edtTextPassword;
    private Button btnLogin, btnSwitchToSignupPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        btnLogin = findViewById(R.id.btnLogin);
        btnSwitchToSignupPage = findViewById(R.id.btn_switch_to_signup_page);
        edtTextUsername = findViewById(R.id.edtText_username_login_id);
        edtTextPassword = findViewById(R.id.edtText_password_login_id);

        edtTextPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    btnLogin.callOnClick();
                }
                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null){
            switch_to_index_page();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTextUsername.getText().toString().equals("") || edtTextPassword.getText().toString().equals("") ){
                    FancyToast.makeText(getApplicationContext(), "Username and Password required...", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(Login_Activity.this);
                    progressDialog.setMessage("Login in progress!!!");
                    progressDialog.show();
                    ParseUser.logInInBackground(edtTextUsername.getText().toString(), edtTextPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null && user != null){
                                FancyToast.makeText(getApplicationContext(), "Welcome back " + user.getUsername(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                switch_to_index_page();
                            }else {
                                FancyToast.makeText(getApplicationContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
        btnSwitchToSignupPage.setOnClickListener(this);
    }

    public void switch_to_index_page(){
        Intent intent = new Intent(Login_Activity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_switch_to_signup_page){
            Intent intent = new Intent(Login_Activity.this, Signup_Activity.class);
            startActivity(intent);
        }
    }

    public void hideKeyboard(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}