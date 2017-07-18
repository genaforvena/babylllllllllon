package org.imozerov.babylonapp.api;


import android.arch.lifecycle.LiveData;

import org.imozerov.babylonapp.api.model.PostJson;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BabylonService {
    @GET("posts")
    LiveData<ApiResponse<PostJson>> getUser(@Path("login") String login);
}
