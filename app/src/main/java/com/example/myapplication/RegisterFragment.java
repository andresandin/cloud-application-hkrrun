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
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.server.RequestLogin;
import com.example.myapplication.server.RequestRegister;
import com.example.myapplication.server.ResponseLogin;
import com.example.myapplication.server.ResponseRegister;
import com.example.myapplication.server.RestAPICommunication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.server.ServerData.BASE_URL;

public class RegisterFragment extends AppCompatActivity {

    private static final String TAG = "RegisterFragment";
    
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mSsn;
    private Button register;

    private RestAPICommunication mRestApiCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        mUsername = findViewById(R.id.registerUsername);
        mPassword = findViewById(R.id.registerPassword);
        mEmail = findViewById(R.id.registerEmail);
        mSsn = findViewById(R.id.registerSsn);
        register = findViewById(R.id.registerBtn);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRestApiCommunicator = retrofit.create(RestAPICommunication.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDataEntered()){
                    register();
                }
            }
        });
    }

    private boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean checkDataEntered() {
        if (isEmpty(mUsername)) {
            mUsername.setError("First name is required!");
            return false;
        }

        if (isEmpty(mPassword)) {
            mPassword.setError("Last name is required!");
            return false;
        }

        if (isEmail(mEmail) == false) {
            mEmail.setError("Enter valid email!");
            return false;
        }

        if (isEmpty(mSsn)) {
            mSsn.setError("Enter a password!");
            return false;
        }
        return true;
    }

    private void register(){
        Call<ResponseRegister> call = mRestApiCommunicator.registerUser(
                buildRequest());
        call.enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                ResponseRegister responseRegister = response.body();
                if( responseRegister != null){
                    if( responseRegister.getStatus().equals("success")) {
                        Log.d(TAG, "onResponse: status: " + responseRegister.getStatus());
                        Log.d(TAG, "onResponse: message: " + responseRegister.getMessage());

                        //Intent intent = new Intent(LogInFragment.this, MainActivity.class);

                        //startActivity(intent);
                    }
                    else{
                        makeToast("register failed");
                        Log.d(TAG,response.toString());
                    }
                }
                else
                    makeToast("register failed");
                Log.d(TAG,response.toString());
                //btnLock();
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                makeToast(t.getMessage());
                // btnLock();
            }
        });
    }

    private RequestRegister buildRequest(){
        return new RequestRegister(
                mUsername.getText().toString(),
                mPassword.getText().toString(),
                mEmail.getText().toString(),
                mSsn.getText().toString(),
                0);
    }

    private void makeToast(String msg){
        Toast.makeText(this , msg, Toast.LENGTH_LONG).show();
    }
}
