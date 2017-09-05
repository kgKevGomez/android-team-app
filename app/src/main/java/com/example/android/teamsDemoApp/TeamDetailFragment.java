package com.example.android.teamsDemoApp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.teamsDemoApp.model.Team;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * A fragment representing a single Team detail screen.
 * This fragment is either contained in a {@link TeamListActivity}
 * in two-pane mode (on tablets) or a {@link TeamDetailActivity}
 * on handsets.
 */
public class TeamDetailFragment extends Fragment implements OnMapReadyCallback {
    public static final String BUNDLE_ITEM = "BUNDLE_ITEM";
    private Team mItem;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private MapView mMapView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamDetailFragment() {
    }

    public static TeamDetailFragment getInstance(Team team){
        Bundle arguments = new Bundle();
        arguments.putParcelable(TeamDetailFragment.BUNDLE_ITEM, Parcels.wrap(team));

        TeamDetailFragment fragment = new TeamDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(BUNDLE_ITEM)) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_ITEM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_team_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.text_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.text_description)).setText(mItem.getDescription());
            ImageView imageTeamView = (ImageView) rootView.findViewById(R.id.image_team);
            Picasso.with(getContext())
                    .load(mItem.getLogoUrl())
                    .placeholder(R.drawable.ic_info_outline_32dp)
                    .fit()
                    .centerInside()
                    .into(imageTeamView);

            mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video_team);
            initializePlayer();
            initializeMap(rootView, savedInstanceState);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        if (mMapView != null)
            mMapView.onResume();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }


    private void initializeMap(View rootView, Bundle savedInstanceState) {
        // Gets the MapView from the XML layout and creates it
        mMapView = (MapView) rootView.findViewById(R.id.map_location);
        mMapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMapView.getMapAsync(this);
    }

    private void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "teamDemoApp"), null);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mItem.getVideoUrl()),
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the mPlayer with the source.
        mPlayer.prepare(videoSource);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());

        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng stadium = new LatLng(mItem.getLatitude(), mItem.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(stadium)
                .title("Stadium"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 12));
    }
}
