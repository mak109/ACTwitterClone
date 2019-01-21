package com.example.mayukh.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmailSignUp,edtUserNameSignUp,edtPasswordSignUp;
    private Button btnSignUpActivity,btnLogInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       //ParseInstallation.getCurrentInstallation().saveInBackground();
        setTitle("Sign Up");
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        edtUserNameSignUp = findViewById(R.id.edtUserNameSignUp);

        btnLogInActivity = findViewById(R.id.btnLogInActivity);
        btnSignUpActivity = findViewById(R.id.btnSignUpActivity);

        btnSignUpActivity.setOnClickListener(SignUp.this);
        btnLogInActivity.setOnClickListener(SignUp.this);

        btnSignUpActivity.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSignUpActivity);
                }
                return true;
            }
        });

        if(ParseUser.getCurrentUser() != null){
            transitionToSocialMediaActivity();
        }


    }

    public void transitionToSocialMediaActivity() {
        Intent intent = new Intent(SignUp.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignUpActivity:
                if(edtUserNameSignUp.getText().toString().equals("") ||
                        edtPasswordSignUp.getText().toString().equals("") ||
                        edtEmailSignUp.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignUp.this,"Username/Password/Email Id required",FancyToast.LENGTH_LONG,
                            FancyToast.INFO,true).show();
                }
                else
                signUp();
                break;
            case R.id.btnLogInActivity:
                logIn();
                break;
        }

    }

    public void logIn() {
        Intent intent = new Intent(SignUp.this,LogIn.class);
        startActivity(intent);


    }

    public void signUp() {
        try {
            final ParseUser appUser = new ParseUser();
            appUser.setEmail(edtEmailSignUp.getText().toString());
            appUser.setUsername(edtUserNameSignUp.getText().toString());
            appUser.setPassword(edtPasswordSignUp.getText().toString());
            final ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setMessage("Signing Up "+edtUserNameSignUp.getText().toString());
            progressDialog.show();
            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, appUser.getUsername() + " is " +
                                "succesfully signed up", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        transitionToSocialMediaActivity();

                    } else {
                        FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }catch (Exception e){
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
        }
    }
    public void rootLayoutSignUpTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
