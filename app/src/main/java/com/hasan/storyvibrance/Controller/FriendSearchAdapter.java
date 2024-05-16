package com.hasan.storyvibrance.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.FriendSearchModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.ViewHolder> {

    private List<FriendSearchModel> searchResults;

    public FriendSearchAdapter(List<FriendSearchModel> searchResults) {
        this.searchResults = searchResults;
    }

    public void setData(List<FriendSearchModel> newData) {
        searchResults.clear();
        searchResults.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_search_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendSearchModel friendSearchModel = searchResults.get(position);
        String name = friendSearchModel.getName();
        String capitalizedName = NameCapitalize.capitalize(name);
        holder.textViewUserName.setText(capitalizedName);
        // Load profile image with Picasso
        if (friendSearchModel.getProfileImg() != null && !friendSearchModel.getProfileImg().isEmpty()) {
            Picasso.get().load(friendSearchModel.getProfileImg())
                    .placeholder(R.drawable.edit_person)
                    .error(R.drawable.edit_person)
                    .into(holder.friendSearchImg);
        } else {
            holder.friendSearchImg.setImageResource(R.drawable.edit_person);
        }

    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        ImageView friendSearchImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            friendSearchImg = itemView.findViewById(R.id.friendSearchImg);
        }
    }
}

