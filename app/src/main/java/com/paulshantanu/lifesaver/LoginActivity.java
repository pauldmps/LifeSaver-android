package com.paulshantanu.lifesaver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shantanu Paul on 14-12-2015. */
public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    View coordinatorLayoutView;
    SharedPreferences loginData;
    Intent nextPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginData = getApplication().getSharedPreferences("loginData", MODE_PRIVATE);
        Log.i("debug","preferences token: "+ loginData.getString("token",""));
        if(!(loginData.getString("loginToken","").equals(null)||loginData.getString("loginToken","").equals(""))){
            checkLogin();
        }

        etUsername = (EditText)findViewById(R.id.email);
        etPassword = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.sign_in_button);
        coordinatorLayoutView = findViewById(R.id.snackbarPosition);

        final List<Pair<String,String>> postData = new ArrayList<>();



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                postData.add(new Pair<>("username",etUsername.getText().toString().trim()));
                postData.add(new Pair<>("password", etPassword.getText().toString().trim()));
                    new APIAccessTask(LoginActivity.this, "https://lifesaver-paulshantanu.rhcloud.com/signin", "POST", postData,
                            new APIAccessTask.OnCompleteListener() {
                        @Override
                        public void onComplete(APIResponseObject result) {
                            if (result.responseCode != HttpURLConnection.HTTP_OK) {
                                showError(result.responseCode);
                            }
                            else {
                                oneTimeLogin(result.response);
                            }
                        }
                    }).execute();
            }
        });
    }

    private void showError(int code){
        Snackbar.make(coordinatorLayoutView,"Error code: " + code,Snackbar.LENGTH_INDEFINITE).show();
    }

    private void oneTimeLogin(String jsonToParse){
        try {
            JSONObject reader = new JSONObject(jsonToParse);
            String userName = reader.getString("username");
            String loginToken = reader.getString("token");

            Log.i("debug","username"+userName);
            Log.i("debug", "token" + loginToken);

            SharedPreferences.Editor editor = loginData.edit();
            editor.putString("username",userName);
            editor.putString("loginToken",loginToken);
            editor.commit();

            openMainPage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    void checkLogin(){

        List<Pair<String,String>> headerData = new ArrayList<>();
        headerData.add(new Pair<>("x-access-token",loginData.getString("loginToken","")));
        headerData.add(new Pair<>("x-auth-username", loginData.getString("username", "")));

        new APIAccessTask(LoginActivity.this, "https://lifesaver-paulshantanu.rhcloud.com/auth/user/", "GET", null, headerData,
                new APIAccessTask.OnCompleteListener() {
                    @Override
                    public void onComplete(APIResponseObject result) {
                        if(result.responseCode == HttpURLConnection.HTTP_OK){
                            openMainPage();
                        }
                    }
                }).execute();


    }

    void openMainPage(){
        nextPage = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(nextPage);
        LoginActivity.this.finish();
    }
}

