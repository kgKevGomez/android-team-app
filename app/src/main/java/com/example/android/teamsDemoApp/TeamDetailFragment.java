package com.example.android.teamsDemoApp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.android.teamsDemoApp.dao.TeamContent;
import com.example.android.teamsDemoApp.model.Team;

/**
 * A fragment representing a single Team detail screen.
 * This fragment is either contained in a {@link TeamListActivity}
 * in two-pane mode (on tablets) or a {@link TeamDetailActivity}
 * on handsets.
 */
public class TeamDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Team mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = TeamContent.ITEM_MAP.get(getArguments().getInt(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_team_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.text_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.text_description)).setText(mItem.getDescription());
            VideoView videoPlayer = (VideoView) rootView.findViewById(R.id.video_team);
            videoPlayer.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
            videoPlayer.seekTo(0);
            videoPlayer.start();
        }

        return rootView;
    }
}
