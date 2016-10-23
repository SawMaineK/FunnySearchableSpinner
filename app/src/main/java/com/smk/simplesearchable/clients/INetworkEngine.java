package com.smk.simplesearchable.clients;

import com.smk.simplesearchable.models.Destination;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface INetworkEngine {

    @GET("/api/v1/destinations")
    void getDestinations(
            @Query("keywords") String keywords,
            @Query("offset") Integer offset,
            @Query("limit") Integer limit,
            Callback<List<Destination>> callback);

}
