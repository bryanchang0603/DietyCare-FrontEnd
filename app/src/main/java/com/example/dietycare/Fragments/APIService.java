package com.example.dietycare.Fragments;

import com.example.dietycare.Notifications.MyResponse;
import com.example.dietycare.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAANRYBe0k:APA91bF4Ukwbp-xuTMdqUXI1LpyiX-ndRKQVvnrOofxATXA0geekKP5-of0iXIsPGFsNVBgvCxpD_wvKnDqZf33pMc1LEfZhvJ1S5uut62iliXxaGzSPwew4aA1ygdxaCRZN1_ngtoN9"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
