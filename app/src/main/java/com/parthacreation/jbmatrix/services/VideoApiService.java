package com.parthacreation.jbmatrix.services;

import com.parthacreation.jbmatrix.models.VideoTabResponse;
import com.parthacreation.jbmatrix.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VideoApiService {
    @Headers(Constants.header)
    @FormUrlEncoded
    @POST("fetch_video_tab_group_wise")
    Call<VideoTabResponse> getVideoTab(@Field("tab_group_id") int tabGroupId);
}
