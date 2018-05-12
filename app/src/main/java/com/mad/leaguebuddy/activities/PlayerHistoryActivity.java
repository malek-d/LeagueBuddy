package com.mad.leaguebuddy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.leaguebuddy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerHistoryActivity extends AppCompatActivity {

    @BindView(R.id.matchHistoryTitleTV)
    TextView mMatchHistoryTitleTV;
    @BindView(R.id.matchHistoryRV)
    RecyclerView mMatchHistoryRV;
    @BindView(R.id.historyLayout)
    LinearLayout mHistoryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_history);
        ButterKnife.bind(this);
    }
}
