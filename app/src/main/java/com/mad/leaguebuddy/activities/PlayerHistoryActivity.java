package com.mad.leaguebuddy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.adapters.MatchAdapter;
import com.mad.leaguebuddy.model.Match;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerHistoryActivity extends AppCompatActivity {


    @BindView(R.id.matchHistoryTitleTV)
    protected TextView mMatchHistoryTitleTV;
    @BindView(R.id.matchHistoryRV)
    protected RecyclerView mMatchHistoryRV;
    @BindView(R.id.historyLayout)
    protected LinearLayout mHistoryLayout;

    private ArrayList<Match> mMatchArrayList = new ArrayList<>();
    private MatchAdapter mMatchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_history);
        ButterKnife.bind(this);


    }
}
