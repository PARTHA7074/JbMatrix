package com.parthacreation.jbmatrix.services;

import com.parthacreation.jbmatrix.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPI {
    static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
