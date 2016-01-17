package com.paulshantanu.lifesaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, confirmPassword;
    Button btnRegister;
    Spinner bloodGroupSpinner;
    CheckBox checkBoxAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText)findViewById(R.id.register_name);
        email = (EditText)findViewById(R.id.register_email);
        password = (EditText)findViewById(R.id.register_password);
        confirmPassword = (EditText)findViewById(R.id.register_confirm_password);
        btnRegister = (Button)findViewById(R.id.button_register);
        bloodGroupSpinner = (Spinner)findViewById(R.id.register_bloodgroup);
        checkBoxAgree = (CheckBox)findViewById(R.id.register_checkbox);

        String bloodGroups[] = {"A+","B+","AB+","O+","A-","B-","AB-","O-"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,bloodGroups);
        bloodGroupSpinner.setAdapter(spinnerAdapter);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().equals(confirmPassword.getText().toString())&& checkBoxAgree.isChecked()){

                   List<Pair<String,String>> postData = new ArrayList<>();
                   postData.add(new Pair<String, String>("name",name.getText().toString()));
                   postData.add(new Pair<>("email",email.getText().toString()));
                   postData.add(new Pair<>("password",password.getText().toString()));
                   postData.add((new Pair<>("bloodGroup",bloodGroupSpinner.getSelectedItem().toString())));

                new APIAccessTask(RegisterActivity.this, "https://lifesaver-paulshantanu.rhcloud.com/register","POST", postData, null, new APIAccessTask.OnCompleteListener() {
                    @Override
                    public void onComplete(APIResponseObject result) {
                        if(result.responseCode == HttpURLConnection.HTTP_OK){
                            Log.i("debug","json: "+ result.response);
                            oneTimeLogin(result.response);
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Error code: "+ result.responseCode,Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
            }
        }});
    }


    private void oneTimeLogin(String jsonToParse){
        try {
            JSONObject reader = new JSONObject(jsonToParse);
            String email = reader.getString("email");
            String loginToken = reader.getString("token");

            Log.i("debug", "username" + email);
            Log.i("debug", "token" + loginToken);

            SharedPreferences loginData = getApplication().getSharedPreferences("loginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = loginData.edit();
            editor.putString("email",email);
            editor.putString("loginToken", loginToken);
            editor.commit();

            Intent nextPage = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(nextPage);
            RegisterActivity.this.finish();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
