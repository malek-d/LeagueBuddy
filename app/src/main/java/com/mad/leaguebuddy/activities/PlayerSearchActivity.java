package com.mad.leaguebuddy.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;

public class PlayerSearchActivity extends AppCompatActivity {

    private MaterialSpinner regionSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regionSpinner = findViewById(R.id.regionSpinner);
        regionSpinner.setItems(getResources().getStringArray(R.array.regions));
    }

}
