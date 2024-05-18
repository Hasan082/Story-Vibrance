package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private List<FriendRequestModel> friendRequests;
    private Context context;

    FirebaseFirestore db;

    public FriendListAdapter(Context context, List<FriendRequestModel> friendRequests) {
        this.context = context;
        this.friendRequests = friendRequests;
    }


    @NonNull
    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.ViewHolder holder, int position) {
        FriendRequestModel friendRequest = friendRequests.get(position);
        db = FirebaseFirestore.getInstance();

        UserDataFetcher userDataFetcher = new UserDataFetcher(db);
        userDataFetcher.fetchUserData(friendRequest.getSenderId(), (name, profileImg) -> {
            String fullName = NameCapitalize.capitalize(name);
            // Update the sender's name and profile image URL in the FriendRequestModel
            friendRequest.setSenderName(fullName);
            friendRequest.setSenderProfileImageUrl(profileImg);

            // Update the UI with the updated data
            holder.friendName.setText(fullName);
            Picasso.get().load(friendRequest.getSenderProfileImageUrl()).placeholder(R.drawable.edit_person)
                    .error(R.drawable.edit_person)
                    .into(holder.friendProfileImage);
        });
        String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(String.valueOf(friendRequest.getTimestamp())));
        holder.requestConfirmedTime.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public void updateFriendRequests(List<FriendRequestModel> friendRequests) {
        this.friendRequests = friendRequests;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friendProfileImage;
        TextView friendName, requestConfirmedTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            requestConfirmedTime = itemView.findViewById(R.id.requestConfirmedTime);
            friendProfileImage = itemView.findViewById(R.id.friendProfileImage);
        }
    }
}
