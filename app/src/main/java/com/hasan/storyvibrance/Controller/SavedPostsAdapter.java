package com.hasan.storyvibrance.Controller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SavedPostsAdapter extends RecyclerView.Adapter<SavedPostsAdapter.PostViewHolder> {

    private final List<PostModel> savedPosts;

    public SavedPostsAdapter(List<PostModel> savedPosts) {
        this.savedPosts = savedPosts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = savedPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return savedPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView savedPostImg;
        private final TextView savedPostTitle;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            savedPostImg = itemView.findViewById(R.id.savedPostImg);
            savedPostTitle = itemView.findViewById(R.id.savedPostTitle);
        }

        public void bind(PostModel post) {
            // Bind post data to views
            Picasso.get().load(post.getPostMedia()).into(savedPostImg);
            savedPostTitle.setText(post.getPostTextContent());
        }
    }
}
