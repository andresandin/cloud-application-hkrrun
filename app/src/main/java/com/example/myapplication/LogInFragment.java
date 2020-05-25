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

    private static final String TAG = "LogInFragment";

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;

    private RestAPICommunication mRestApiCommunicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        username = findViewById(R.id.userNameLN);
        password = findViewById(R.id.passwordLN);
        login = findViewById(R.id.loginBtnLN);
        register = findViewById(R.id.registerBtnLN);

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkUsername();
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
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

                        Intent intent = new Intent(LogInFragment.this, MainActivity.class);

                        startActivity(intent);
                    }
                    else{
                        makeToast("login failed");
                        Log.d(TAG,response.toString());
                    }
                }
                else
                    makeToast("login failed");
                Log.d(TAG,response.toString());
                //btnLock();
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                makeToast(t.getMessage());
               // btnLock();
            }
        });
    }

    void checkUsername() {
        boolean isValid = true;
        if (isEmpty(username)) {
            username.setError("You must enter username to login!");
            isValid = false;
        } else {
            if (isEmpty(username)) { //!isEmail
                username.setError("Enter valid email!");
                isValid = false;
            }
        }

        if (isEmpty(password)) {
            password.setError("You must enter password to login!");
            isValid = false;
        } else {
            if (password.getText().toString().length() < 1) {
                password.setError("Password must be at least 4 chars long!");
                isValid = false;
            }
        }

        //check email and password
        //IMPORTANT: here should be call to backend or safer function for local check; For example simple check is cool
        //For example simple check is cool
        if (isValid) {
            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();
            if (usernameValue.equals("a") && passwordValue.equals("a")) {
                //everything checked we open new activity
                Intent i = new Intent(LogInFragment.this, MainActivity.class);
                startActivity(i);
                //we close this activity
                this.finish();
            } else {
                Toast t = Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private RequestLogin buildRequest(){
        return new RequestLogin(
                username.getText().toString(),
                password.getText().toString());
    }

    private void makeToast(String msg){
        Toast.makeText(this , msg, Toast.LENGTH_LONG).show();
    }

}

