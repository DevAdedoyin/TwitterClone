package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Signup_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTxtEmail, edtTxtUsername, edtTxtPassword, edtTxtCpassword;
    private Button btnSignup, btnSwitchToLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        edtTxtEmail = findViewById(R.id.edtText_email_signup_id);
        edtTxtUsername = findViewById(R.id.edtText_username_signup_id);
        edtTxtPassword = findViewById(R.id.edtText_password_signup_id);
        edtTxtCpassword = findViewById(R.id.edtText_cpassword_signup_id);

        edtTxtCpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    btnSignup.callOnClick();
                }
                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null){
            switch_to_index_page();
        }

        btnSignup = findViewById(R.id.btnSignup);
        btnSwitchToLoginPage = findViewById(R.id.btn_switch_to_login_page);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTxtCpassword.getText().toString() != edtTxtPassword.getText().toString() && edtTxtPassword.getText().toString().length() < 8) {
                    FancyToast.makeText(getApplicationContext(), "Password and Confirm Password must be equal and must be 8 or more characters long", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                } else {
                    if (edtTxtUsername.getText().equals("") || edtTxtUsername.getText().equals("") || edtTxtPassword.getText().equals("") || edtTxtCpassword.getText().equals("")) {
                        FancyToast.makeText(getApplicationContext(), "Email, Username, Password and Confirm Password are all required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    } else {
                        ParseUser parseUser = new ParseUser();
                        parseUser.setEmail(edtTxtEmail.getText().toString());
                        parseUser.setUsername(edtTxtUsername.getText().toString());
                        parseUser.setPassword(edtTxtPassword.getText().toString());
                        parseUser.put("Confirm_Password", edtTxtCpassword.getText().toString());
                        final ProgressDialog progressDialog = new ProgressDialog(Signup_Activity.this);
                        progressDialog.setMessage("Registering " + edtTxtUsername.getText().toString());
                        progressDialog.show();
                        parseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    FancyToast.makeText(getApplicationContext(), edtTxtUsername.getText().toString() + " Registration successful", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                    switch_to_index_page();
                                }else{
                                    FancyToast.makeText(getApplicationContext(), e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
        btnSwitchToLoginPage.setOnClickListener(this);
    }

    public void switch_to_index_page(){
        Intent intent = new Intent(Signup_Activity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }

    public void hideKeyboard(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_switch_to_login_page){
            Intent intent = new Intent(Signup_Activity.this, Login_Activity.class);
            startActivity(intent);
        }
    }
}