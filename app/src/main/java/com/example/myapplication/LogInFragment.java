package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.ClientInformation;
import com.example.myapplication.server.RequestLogin;
import com.example.myapplication.server.ResponseLogin;
import com.example.myapplication.server.RestAPICommunication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.server.ServerData.BASE_URL;

public class LogInFragment extends AppCompatActivity {

    //Used for logging
    private static final String TAG = "LogInFragment";

    //UI variables
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;

    //Used for communication with the REST API
    private RestAPICommunication mRestApiCommunicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        mUsername = findViewById(R.id.userNameLN);
        mPassword = findViewById(R.id.passwordLN);
        mLogin = findViewById(R.id.loginBtnLN);
        mRegister = findViewById(R.id.registerBtnLN);

        setupListeners();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mRestApiCommunicator = retrofit.create(RestAPICommunication.class);



    }

    private void setupListeners() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUsername()){
                    login();
                }else{
                    makeToast("Input was not correct!");
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInFragment.this, RegisterFragment.class);
                startActivity(i);

            }
        });
    }

    private void login(){
        Call<ResponseLogin> call = mRestApiCommunicator.login(
                buildRequest());
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseLogin = response.body();
                if( responseLogin != null){
                    if( responseLogin.getToken() != null) {
                        Log.d(TAG, "onResponse: token: " + responseLogin.getToken());
                        Log.d(TAG, "onResponse: privilege: " + responseLogin.getPrivilege());

                        //Set token to singleton!
                        ClientInformation.getInstance().setToken("Bearer " + responseLogin.getToken());

                        //Start main activity
                        Intent intent = new Intent(LogInFragment.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        makeToast("login failed");
                        Log.d(TAG,response.toString());
                    }

                }else {
                    makeToast("login failed");
                }
                Log.d(TAG, response.toString());
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }

    private boolean checkUsername() {
        boolean isValid = true;
        if (isEmpty(mUsername)) {
            mUsername.setError("You must enter username to login!");
            isValid = false;
        }

        if (isEmpty(mPassword)) {
            mPassword.setError("You must enter password to login!");
            isValid = false;
        }

        return isValid;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private RequestLogin buildRequest(){
        return new RequestLogin(
                mUsername.getText().toString(),
                mPassword.getText().toString());
    }

    private void makeToast(String msg){
        Toast.makeText(this , msg, Toast.LENGTH_LONG).show();
    }

}

