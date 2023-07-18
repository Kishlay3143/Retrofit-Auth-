package com.example.retrofitauthentication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofitauthentication.R;
import com.example.retrofitauthentication.ResponseModel.LoginResponse;
import com.example.retrofitauthentication.ResponseModel.UpdatePassResponse;
import com.example.retrofitauthentication.RetrofitClient;
import com.example.retrofitauthentication.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    EditText etUserName,etUserEmail,etCurrentPass,etConfirmPass;
    Button updateUserAccountBtn,btnUpdatePassword;
    SharedPrefManager sharedPrefManager;
    int userId;
    String userEmailId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        // Update User
        etUserName= view.findViewById(R.id.userName_profile);
        etUserEmail= view.findViewById(R.id.email_profile);
        updateUserAccountBtn= view.findViewById(R.id.btnUpdateAccount);

        sharedPrefManager= new SharedPrefManager(getActivity());
        userId= sharedPrefManager.getUser().getId();

        // Update Password
        etCurrentPass= view.findViewById(R.id.etCurrentPass);
        etConfirmPass= view.findViewById(R.id.etConfirmPass);
        btnUpdatePassword= view.findViewById(R.id.btnUpdatePassword);
        userEmailId= sharedPrefManager.getUser().getEmail();

        updateUserAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserAccount();
            }
        });

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserPassword();
            }
        });

        return view;
    }

    private void updateUserAccount(){
            String username=etUserName.getText().toString().trim();
            String email=etUserEmail.getText().toString().trim();

            if (username.isEmpty()){
                etUserName.setError("Please Enter Your UserName");
                etUserName.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etUserEmail.requestFocus();
                etUserEmail.setError("Please Enter Correct Email");
                return;
            }

        Call<LoginResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .updateUserAccount(userId,username,email);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse updateResponse= response.body();
                if (response.isSuccessful()){

                    if (updateResponse.getError().equals("200")){
                        sharedPrefManager.saveUser(updateResponse.getUser());
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserPassword(){
        String userCurrentPass=etCurrentPass.getText().toString().trim();
        String userNewPass=etConfirmPass.getText().toString().trim();

        if (userCurrentPass.isEmpty()){
            etCurrentPass.setError("Enter Current Password");
            etCurrentPass.requestFocus();
            return;
        }

        if (userCurrentPass.length()<8){
            etCurrentPass.setError("Enter 8 Digit Password");
            etCurrentPass.requestFocus();
            return;
        }

        if (userNewPass.isEmpty()){
            etConfirmPass.setError("Enter New Password");
            etConfirmPass.requestFocus();
            return;
        }

        if (userNewPass.length()<8){
            etConfirmPass.setError("Enter 8 Digit Password");
            etConfirmPass.requestFocus();
            return;
        }

        Call<UpdatePassResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .updateUserPass(userEmailId,userCurrentPass,userNewPass);

        call.enqueue(new Callback<UpdatePassResponse>() {
            @Override
            public void onResponse(Call<UpdatePassResponse> call, Response<UpdatePassResponse> response) {

                UpdatePassResponse passwordResponse= response.body();
                if (response.isSuccessful()){

                    if (passwordResponse.getError().equals("200")){
                        Toast.makeText(getActivity(), passwordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePassResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}