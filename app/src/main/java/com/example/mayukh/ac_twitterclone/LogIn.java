package com.example.mayukh.ac_twitterclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmailLogIn, edtPasswordLogIn;
    private Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("Log In");

        edtEmailLogIn = findViewById(R.id.edtEmailLogIn);
        edtPasswordLogIn = findViewById(R.id.edtPasswordLogIn);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(LogIn.this);
        btnLogIn.setOnClickListener(LogIn.this);

        if(ParseUser.getCurrentUser() != null) {
            transitionToSocialMediaActivity();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(LogIn.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogIn:
                if (edtEmailLogIn.getText().toString().equals("") || edtPasswordLogIn.getText().toString().equals("")) {
                    FancyToast.makeText(LogIn.this, "Password/Email Id required", FancyToast.LENGTH_LONG,
                            FancyToast.INFO, true).show();
                } else
                    logInUser();
                break;
            case R.id.btnSignUp:
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
                break;
        }

    }

    private void logInUser() {
        ParseUser appUser = new ParseUser();
        appUser.logInInBackground(edtEmailLogIn.getText().toString(), edtPasswordLogIn.getText().toString()
                , new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            FancyToast.makeText(LogIn.this, user.get("username") +
                                    " is successfully logged in", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            transitionToSocialMediaActivity();
                        } else {
                            FancyToast.makeText(LogIn.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                        }
                    }
                });

    }

    public void rootLayoutLogInTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
