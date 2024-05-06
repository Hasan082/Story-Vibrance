package com.hasan.storyvibrance.Utility;

import com.hasan.storyvibrance.Model.PostModel;

import java.util.Comparator;
import java.util.List;

public class PostSorter {
    public static void sortByTimestampDescending(List<PostModel> posts) {
        posts.sort(new Comparator<PostModel>() {
            @Override
            public int compare(PostModel post1, PostModel post2) {
                return Long.compare(Long.parseLong(post2.getTimestamp()), Long.parseLong(post1.getTimestamp()));
            }
        });
    }
}
