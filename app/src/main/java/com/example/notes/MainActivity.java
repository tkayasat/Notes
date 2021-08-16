package com.example.notes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.notes, Notes.newInstance()).
                commit();
   }

    /*@Override
    protected void onResume() {
        super.onResume();

        Fragment backStackFragment = (Fragment) getSupportFragmentManager()
                .findFragmentById(R.id.cities_container);

        if (backStackFragment != null && backStackFragment instanceof CoatOfArmsFragment) {

            onBackPressed();
        }*/
    }
