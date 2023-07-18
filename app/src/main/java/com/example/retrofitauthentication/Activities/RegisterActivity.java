package com.example.retrofitauthentication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.retrofitauthentication.ResponseModel.RegisterResponse;
import com.example.retrofitauthentication.RetrofitClient;
import com.example.retrofitauthentication.databinding.ActivityRegisterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });

        binding.loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String userName = binding.userNameSignUP.getText().toString();
        String userEmail = binding.emailSignUP.getText().toString();
        String userPassword = binding.passwordSignUP.getText().toString();

        if (userName.isEmpty()) {
            binding.userNameSignUP.requestFocus();
            binding.userNameSignUP.setError("Please Enter Your Name");
            return;
        }
        if (userEmail.isEmpty()) {
            binding.emailSignUP.requestFocus();
            binding.emailSignUP.setError("Please Enter Your Name");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.emailSignUP.requestFocus();
            binding.emailSignUP.setError("Please Enter Correct Email");
            return;
        }
        if (userPassword.isEmpty()) {
            binding.passwordSignUP.requestFocus();
            binding.passwordSignUP.setError("Please Enter Your Password");
            return;
        }
        if (userPassword.length() < 8) {
            binding.passwordSignUP.requestFocus();
            binding.passwordSignUP.setError("Password should be greater than 8 digits");
            return;
        }

        // Retrofit Code
        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .register(userName, userEmail, userPassword);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();

                if (response.isSuccessful()) {
                    Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}