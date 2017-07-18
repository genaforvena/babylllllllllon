package org.imozerov.babylonapp.api;


import android.arch.lifecycle.LiveData;

import org.imozerov.babylonapp.api.model.PostJson;

import java.util.List;

import retrofit2.http.GET;

public interface BabylonService {
    @GET("posts")
    LiveData<ApiResponse<List<PostJson>>> posts();
}
