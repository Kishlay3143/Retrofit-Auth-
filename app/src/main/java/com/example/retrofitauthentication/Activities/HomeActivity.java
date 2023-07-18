package com.example.retrofitauthentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.retrofitauthentication.Fragments.DashboardFragment;
import com.example.retrofitauthentication.Fragments.ProfileFragment;
import com.example.retrofitauthentication.Fragments.UsersFragment;
import com.example.retrofitauthentication.R;
import com.example.retrofitauthentication.ResponseModel.DeleteResponse;
import com.example.retrofitauthentication.RetrofitClient;
import com.example.retrofitauthentication.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView= findViewById(R.id.bottomNav);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new DashboardFragment());

        sharedPrefManager= new SharedPrefManager(getApplicationContext());

    }

    void loadFragment(Fragment fragment){
        // To attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout,fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment=null;

        int id=item.getItemId();

        if(id==R.id.dashboard){
            loadFragment(new DashboardFragment());
        }
        else if(id==R.id.users){
            loadFragment(new UsersFragment());
        }
        else if(id==R.id.profile){
            loadFragment(new ProfileFragment());
        }


        if (fragment!=null){
            loadFragment(fragment);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.logout){
            logoutUser();
        }
        if (item.getItemId()==R.id.deleteAccount){
            deleteAccount();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        sharedPrefManager.logout();
        Intent intent= new Intent(HomeActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "You Have Been Logged Out", Toast.LENGTH_SHORT).show();
    }

    private void deleteAccount() {
        Call<DeleteResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .deleteUser(sharedPrefManager.getUser().getId());

        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse= response.body();

                if (response.isSuccessful()) {

                    if (deleteResponse.getError().equals("200")) {
                        logoutUser(); // it will logout the user's session
                        Toast.makeText(HomeActivity.this, deleteResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(HomeActivity.this, deleteResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(HomeActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}