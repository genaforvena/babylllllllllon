package org.imozerov.babylonapp.api;


import android.arch.lifecycle.LiveData;

import org.imozerov.babylonapp.api.model.CommentJson;
import org.imozerov.babylonapp.api.model.PostJson;
import org.imozerov.babylonapp.api.model.UserJson;

import java.util.List;

import retrofit2.http.GET;

public interface BabylonService {
    @GET("posts")
    LiveData<ApiResponse<List<PostJson>>> posts();
    @GET("comments")
    LiveData<ApiResponse<List<CommentJson>>> comments();
    @GET("users")
    LiveData<ApiResponse<List<UserJson>>> users();
}
