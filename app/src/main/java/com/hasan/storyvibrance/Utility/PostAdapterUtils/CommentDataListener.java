package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import com.hasan.storyvibrance.Model.CommentModel;

import java.util.List;

public interface CommentDataListener {
    void onCommentsFetched(List<CommentModel> comments);
}

