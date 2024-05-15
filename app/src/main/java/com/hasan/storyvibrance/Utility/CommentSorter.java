package com.hasan.storyvibrance.Utility;

import com.hasan.storyvibrance.Model.CommentModel;

import java.util.Comparator;
import java.util.List;

public class CommentSorter {
    public static void sortByTimestampDescending(List<CommentModel> comments) {
        comments.sort(new Comparator<CommentModel>() {
            @Override
            public int compare(CommentModel comment1, CommentModel comment2) {
                return Long.compare(comment2.getTimestamp(), comment1.getTimestamp());
            }
        });
    }
}
