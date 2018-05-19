package com.mad.leaguebuddy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.FirebaseFactory;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseFactory mFirebaseFactory = FirebaseFactory.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
