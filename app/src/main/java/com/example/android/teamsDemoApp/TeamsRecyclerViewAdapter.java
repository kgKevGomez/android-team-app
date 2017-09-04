package com.example.android.teamsDemoApp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.teamsDemoApp.model.Team;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kevin on 9/2/2017.
 */

public class TeamsRecyclerViewAdapter extends RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder> {
    public interface OnTeamClickedListener {
        void onTeamClicked(Team team);
    }

    private final OnTeamClickedListener mOnClickListener;
    private ArrayList<Team> mValues;

    public TeamsRecyclerViewAdapter(OnTeamClickedListener onClickListener, ArrayList<Team> items) {
        mOnClickListener = onClickListener;
        mValues = items;

    }

    public void setData(ArrayList<Team> data){
        mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleTextView.setText(holder.mItem.getName());
        holder.mAddressTextView.setText(holder.mItem.getAddress());
        Picasso.with(holder.mContext)
                .load(holder.mItem.getLogoUrl())
                .fit()
                .centerInside()
                .into(holder.mImageTeamView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleTextView;
        final TextView mAddressTextView;
        private final ImageView mImageTeamView;
        private final Context mContext;
        Team mItem;

        ViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.text_tile);
            mAddressTextView = (TextView) view.findViewById(R.id.text_address);
            mImageTeamView = (ImageView) view.findViewById(R.id.image_team);
            mContext = view.getContext();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onTeamClicked(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddressTextView.getText() + "'";
        }
    }
}