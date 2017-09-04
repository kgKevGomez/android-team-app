package com.example.android.teamsDemoApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.android.teamsDemoApp.dao.TeamContent;
import com.example.android.teamsDemoApp.model.Team;
import com.example.android.teamsDemoApp.services.RestClient;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.teamsDemoApp.TeamDetailActivity.TEAM_PHONE_NUMBER;

/**
 * An activity representing a list of Teams. This activity
 * has a different presentation in landscape mode. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TeamDetailActivity} representing item description.
 * In landscape mode, the activity presents the list of items and
 * item description side-by-side using two vertical panes.
 */
public class TeamListActivity extends AppCompatActivity implements TeamsRecyclerViewAdapter.OnTeamClickedListener {

    public static final String TEAMS_LIST = "TEAMS_LIST";
    private Team currentTeam;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running in landscape mode
     */
    private boolean mTwoPane;
    private ArrayList<Team> teamsList;
    private TeamsRecyclerViewAdapter teamsRecyclerViewAdapter;

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

        loadTeams(savedInstanceState);
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.team_detail_container) != null) {
            // The detail container view will be present only in the landscape mode.
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
            if (savedInstanceState != null)
                currentTeam = Parcels.unwrap(savedInstanceState.getParcelable(TeamDetailFragment.ARG_ITEM_ID));
            else
                currentTeam = teamsList.isEmpty() ? null : teamsList.get(0);

            loadTeam(currentTeam);
        }
    }

    private void loadTeams(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            teamsList = Parcels.unwrap(savedInstanceState.getParcelable(TEAMS_LIST));

        if (teamsList == null || teamsList.isEmpty()){
            Call<ArrayList<Team>> call = RestClient.getInstance().getTeamService().getTeams();
            call.enqueue(new Callback<ArrayList<Team>>() {
                @Override
                public void onResponse(Call<ArrayList<Team>> call, Response<ArrayList<Team>> response) {
                    teamsList = response.body();
                    teamsRecyclerViewAdapter.setData(teamsList);
                    currentTeam = teamsList.isEmpty() ? null : teamsList.get(0);
                }

                @Override
                public void onFailure(Call<ArrayList<Team>> call, Throwable t) {
                    Log.e("TeamDetailActivity", "onFailure: ", t );
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentTeam != null)
            outState.putParcelable(TeamDetailFragment.ARG_ITEM_ID, Parcels.wrap(currentTeam));

        if (teamsList != null && !teamsList.isEmpty())
            outState.putParcelable(TEAMS_LIST, Parcels.wrap(teamsList));

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (mTwoPane)
            getMenuInflater().inflate(R.menu.menu_team_detail, menu);
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        teamsRecyclerViewAdapter = new TeamsRecyclerViewAdapter(this, teamsList == null ? new ArrayList<Team>() : teamsList );
        recyclerView.setAdapter(teamsRecyclerViewAdapter);
    }

    @Override
    public void onTeamClicked(Team team) {
        loadTeam(team);
    }

    private void loadTeam(Team team) {
        currentTeam = team;

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(TeamDetailFragment.ARG_ITEM_ID, Parcels.wrap(team));
            TeamDetailFragment fragment = new TeamDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.team_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, TeamDetailActivity.class);
            intent.putExtra(TeamDetailFragment.ARG_ITEM_ID, Parcels.wrap(team));
            intent.putExtra(TEAM_PHONE_NUMBER, team.getPhoneNumber());

            startActivity(intent);
        }
    }
}
