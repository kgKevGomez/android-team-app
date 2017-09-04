package com.example.android.teamsDemoApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.android.teamsDemoApp.dao.TeamContent;
import com.example.android.teamsDemoApp.model.Team;

/**
 * An activity representing a list of Teams. This activity
 * has a different presentation in landscape mode. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TeamDetailActivity} representing item description.
 * In landscape mode, the activity presents the list of items and
 * item description side-by-side using two vertical panes.
 */
public class TeamListActivity extends AppCompatActivity implements TeamsRecyclerViewAdapter.OnTeamClickedListener {

    private int currentId;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running in landscape mode
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        getSupportActionBar().setHomeButtonEnabled(true);

        View recyclerView = findViewById(R.id.team_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.team_detail_container) != null) {
            // The detail container view will be present only in the landscape mode.
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
            if (savedInstanceState != null)
                currentId = savedInstanceState.getInt(TeamDetailFragment.ARG_ITEM_ID, 0);
            else
                currentId = 0;

            loadTeam();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentId != 0)
            outState.putInt(TeamDetailFragment.ARG_ITEM_ID, currentId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new TeamsRecyclerViewAdapter(this, TeamContent.ITEMS));
    }

    @Override
    public void onTeamClicked(Team team) {
        currentId = team.getId();
        loadTeam();
    }

    private void loadTeam() {
        if (currentId == 0)
            currentId = TeamContent.ITEMS.get(0).getId();

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(TeamDetailFragment.ARG_ITEM_ID, currentId);
            TeamDetailFragment fragment = new TeamDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.team_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, TeamDetailActivity.class);
            intent.putExtra(TeamDetailFragment.ARG_ITEM_ID, currentId);

            startActivity(intent);
        }
    }
}
