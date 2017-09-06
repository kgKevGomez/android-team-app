package com.example.android.teamsDemoApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.android.teamsDemoApp.model.Schedule;
import com.example.android.teamsDemoApp.model.Team;

import org.parceler.Parcels;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * An activity representing a single Team detail screen. This
 * activity is only used in portrait mode. In landscape mode,
 * item description are presented side-by-side with a list of items
 * in a {@link TeamListActivity}.
 */
public class TeamDetailActivity extends AppCompatActivity {

    public static final String TEAM_PHONE_NUMBER = "TEAM_PHONE_NUMBER";
    private Team mTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If orientation is in Landscape mode, then we go back to the
        // main activity to use the side-by-side layout
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            finish();
        }

        setContentView(R.layout.activity_team_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mTeam = Parcels.unwrap(getIntent().getParcelableExtra(TeamDetailFragment.BUNDLE_ITEM));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.team_detail_container, TeamDetailFragment.getInstance(mTeam))
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        else if (id == R.id.call_team){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mTeam.getPhoneNumber()));
            startActivity(intent);
        }
        else if (id == R.id.share_schedule){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Scheduled Games");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mTeam.getName())
                    .append(" Scheduled Games")
                    .append("\n");

            for(Schedule schedule : mTeam.getScheduleGames())
                stringBuilder.append(schedule).append("\n");

            intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());

            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Choose an application:"));
        }

        return super.onOptionsItemSelected(item);
    }
}
