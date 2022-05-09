package com.example.pangeaappproduccion.Util.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAv-g-1UA:APA91bFR2frYGt4WPzSACY4Yhh-nO8K64dx7qtR8yAKTKlZ7oM3ongCOi6pQBLZOun2TBgK9KUSL_WWlhaVWbXz2l4H-XIfEdMcjWBXuMv5gy_uF3Y0DmSMOq1UrmUrnu3lwnPHN2uK9"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
