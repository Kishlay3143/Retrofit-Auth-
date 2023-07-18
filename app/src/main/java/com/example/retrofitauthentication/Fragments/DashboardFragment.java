package com.example.retrofitauthentication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retrofitauthentication.R;
import com.example.retrofitauthentication.SharedPrefManager;


public class DashboardFragment extends Fragment {

    TextView etName,etEmail;
    SharedPrefManager sharedPrefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);
        etName= view.findViewById(R.id.etName);
        etEmail= view.findViewById(R.id.etEmail);

        sharedPrefManager= new SharedPrefManager(getActivity());

        String userName="Hey! " + sharedPrefManager.getUser().getUsername();
        etName.setText(userName);
        etEmail.setText(sharedPrefManager.getUser().getEmail());

        return view;
    }

}