package com.example.retrofitauthentication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.retrofitauthentication.R;
import com.example.retrofitauthentication.ResponseModel.LoginResponse;
import com.example.retrofitauthentication.RetrofitClient;
import com.example.retrofitauthentication.SharedPrefManager;
import com.example.retrofitauthentication.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPrefManager=new SharedPrefManager(getApplicationContext());


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userLogin();
            }
        });

        binding.registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin(){
        String userEmail = binding.emailLogin.getText().toString();
        String userPassword = binding.passwordLogin.getText().toString();


        if (userEmail.isEmpty()) {
            binding.emailLogin.requestFocus();
            binding.emailLogin.setError("Please Enter Your Name");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.emailLogin.requestFocus();
            binding.emailLogin.setError("Please Enter Correct Email");
            return;
        }
        if (userPassword.isEmpty()) {
            binding.passwordLogin.requestFocus();
            binding.passwordLogin.setError("Please Enter Your Password");
            return;
        }
        if (userPassword.length() < 8) {
            binding.passwordLogin.requestFocus();
            binding.passwordLogin.setError("Password should be greater than 8 digits");
            return;
        }

        Call<LoginResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .login(userEmail, userPassword);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse=response.body();
                if (response.isSuccessful()) {

                    if (loginResponse.getError().equals("200")) { // if we'll not pass 200 here. Then, it will show wrong credentials.

                        sharedPrefManager.saveUser(loginResponse.getUser());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPrefManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}