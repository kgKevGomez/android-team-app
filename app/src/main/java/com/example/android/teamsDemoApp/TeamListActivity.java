package com.example.android.teamsDemoApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.android.teamsDemoApp.model.Team;
import com.example.android.teamsDemoApp.services.RestClient;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

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
public class TeamListActivity extends AppCompatActivity implements TeamsRecyclerViewAdapter.OnTeamClickedListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String BUNDLE_TEAMS_LIST = "BUNDLE_TEAMS_LIST";
    public static final String BUNDLE_FETCHED_TEAMS_LIST = "BUNDLE_FETCHED_TEAMS_LIST";

    private Team mCurrentTeam;

    private boolean mTwoPane;
    private ArrayList<Team> mTeamsList;
    private ArrayList<Team> mFetchedTeams; //Since there is no pagination provided by the service, this will be simulated
    private TeamsRecyclerViewAdapter mTeamsRecyclerViewAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        getSupportActionBar().setHomeButtonEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_container);
        //mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeLoad1, R.color.swipeLoad2, R.color.swipeLoad3, R.color.swipeLoad4);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        View recyclerView = findViewById(R.id.team_list);
        loadTeams(savedInstanceState);
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.team_detail_container) != null) {
            // The detail container view will be present only in the landscape mode.
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
            if (savedInstanceState != null)
                mCurrentTeam = Parcels.unwrap(savedInstanceState.getParcelable(TeamDetailFragment.BUNDLE_ITEM));
            else
                mCurrentTeam = mFetchedTeams == null || mFetchedTeams.isEmpty() ? null : mFetchedTeams.get(0);

            loadTeam(mCurrentTeam);
        }
    }

    private void loadTeams(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mTeamsList = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_TEAMS_LIST));
            mFetchedTeams = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_FETCHED_TEAMS_LIST));
        }

        if (mTeamsList == null || mTeamsList.isEmpty()){
            Call<ArrayList<Team>> call = RestClient.getInstance().getTeamService().getTeams();
            call.enqueue(new Callback<ArrayList<Team>>() {
                @Override
                public void onResponse(Call<ArrayList<Team>> call, Response<ArrayList<Team>> response) {
                    mTeamsList = response.body();
                    mFetchedTeams = new ArrayList<Team>();
                    for (int x=mTeamsList.size()-10; x < mTeamsList.size(); x++) //To simulate pagination, first 10 items are taken
                        mFetchedTeams.add(mTeamsList.get(x));

                    mTeamsRecyclerViewAdapter.setData(mFetchedTeams);
                    mCurrentTeam = mFetchedTeams.isEmpty() ? null : mFetchedTeams.get(0);
                    loadTeam(mCurrentTeam);
                }

                @Override
                public void onFailure(Call<ArrayList<Team>> call, Throwable t) {
                    //TODO: add snackbar
                    Log.e("TeamDetailActivity", "onFailure: ", t );
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCurrentTeam != null)
            outState.putParcelable(TeamDetailFragment.BUNDLE_ITEM, Parcels.wrap(mCurrentTeam));

        if (mTeamsList != null && !mTeamsList.isEmpty()) {
            outState.putParcelable(BUNDLE_TEAMS_LIST, Parcels.wrap(mTeamsList));
            outState.putParcelable(BUNDLE_FETCHED_TEAMS_LIST, Parcels.wrap(mFetchedTeams));
        }

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
        mTeamsRecyclerViewAdapter = new TeamsRecyclerViewAdapter(this, mFetchedTeams == null ? new ArrayList<Team>() : mFetchedTeams);
        recyclerView.setAdapter(mTeamsRecyclerViewAdapter);
    }

    @Override
    public void onTeamClicked(Team team) {
        loadTeam(team);
    }

    private void loadTeam(Team team) {
        mCurrentTeam = team;

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(TeamDetailFragment.BUNDLE_ITEM, Parcels.wrap(team));
            TeamDetailFragment fragment = new TeamDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.team_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, TeamDetailActivity.class);
            intent.putExtra(TeamDetailFragment.BUNDLE_ITEM, Parcels.wrap(team));

            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        if (mTeamsList == null || mTeamsList.size() == 0)
            return;

        //Normally we would check if last page reached end of pagination
        //but since we don't have a really pagination we will do it this way
        if (mFetchedTeams.size() != mTeamsList.size()){
            for(int x= mTeamsList.size()- mFetchedTeams.size()-1; x > Math.max(-1,mTeamsList.size()- mFetchedTeams.size()-10); x--)
                mFetchedTeams.add(0, mTeamsList.get(x));

            mTeamsRecyclerViewAdapter.setData(mFetchedTeams);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        else
            mSwipeRefreshLayout.setRefreshing(false);
    }
}
